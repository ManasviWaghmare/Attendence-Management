package com.qrattendance.service;

import com.qrattendance.dto.QRResponse;
import com.qrattendance.model.Course;
import com.qrattendance.model.Session;
import com.qrattendance.repository.CourseRepository;
import com.qrattendance.repository.SessionRepository;
import com.qrattendance.util.QRGenerator;
import com.qrattendance.util.QRValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class QRService {

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final QRGenerator qrGenerator;
    private final QRValidator qrValidator;

    public QRService(SessionRepository sessionRepository, CourseRepository courseRepository, QRGenerator qrGenerator, QRValidator qrValidator) {
        this.sessionRepository = sessionRepository;
        this.courseRepository = courseRepository;
        this.qrGenerator = qrGenerator;
        this.qrValidator = qrValidator;
    }

    @Value("${qr.expiry.minutes:5}")
    private int qrExpiryMinutes;

    @Transactional
    public QRResponse generateAndSaveQR(Long courseId) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Session session = sessionRepository.findByCourseAndActive(course, true)
                .stream().findFirst()
                .orElseGet(() -> createNewSession(course));

        String rawToken = String.format("session:%d|time:%s|nonce:%s",
                session.getId(),
                LocalDateTime.now().toString(),
                UUID.randomUUID().toString());

        String encryptedToken = qrValidator.encrypt(rawToken);

        session.setCurrentQrCode(encryptedToken);
        session.setQrExpiryTime(LocalDateTime.now().plusMinutes(qrExpiryMinutes));
        sessionRepository.save(session);

        String qrImage = qrGenerator.generateQRCodeBase64(encryptedToken, 400, 400);
        
        return QRResponse.builder()
                .qrImage(qrImage)
                .sessionId(session.getId())
                .token(encryptedToken)
                .build();
    }

    private Session createNewSession(Course course) {
        Session session = Session.builder()
                .course(course)
                .startTime(LocalDateTime.now())
                .active(true)
                .build();
        return sessionRepository.save(session);
    }

    public QRResponse getCurrentSessionQR(Long sessionId) throws Exception {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        if (session.getCurrentQrCode() == null) {
            return generateAndSaveQR(session.getCourse().getId());
        }
        
        String qrImage = qrGenerator.generateQRCodeBase64(session.getCurrentQrCode(), 400, 400);
        return QRResponse.builder()
                .qrImage(qrImage)
                .sessionId(session.getId())
                .token(session.getCurrentQrCode())
                .build();
    }
}
