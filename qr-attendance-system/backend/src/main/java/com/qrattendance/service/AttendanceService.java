package com.qrattendance.service;

import com.qrattendance.dto.AttendanceRequest;
import com.qrattendance.model.Attendance;
import com.qrattendance.model.Session;
import com.qrattendance.model.Student;
import com.qrattendance.repository.AttendanceRepository;
import com.qrattendance.repository.SessionRepository;
import com.qrattendance.repository.StudentRepository;
import com.qrattendance.util.QRValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AttendanceService {
    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final StudentRepository studentRepository;
    private final QRValidator qrValidator;

    public AttendanceService(AttendanceRepository attendanceRepository, SessionRepository sessionRepository, StudentRepository studentRepository, QRValidator qrValidator) {
        this.attendanceRepository = attendanceRepository;
        this.sessionRepository = sessionRepository;
        this.studentRepository = studentRepository;
        this.qrValidator = qrValidator;
    }

    @Transactional
    public void markAttendance(AttendanceRequest request) {
        log.info("Processing attendance for roll: {} with QR: {}", request.getRollNumber(), request.getQrCode());
        String decrypted = qrValidator.decrypt(request.getQrCode());
        if (decrypted == null) {
            throw new RuntimeException("Invalid QR Code (Decryption failed)");
        }

        String[] parts = decrypted.split("\\|");
        String sessionIdPart = parts[0].split(":")[1];
        Long sessionId = Long.parseLong(sessionIdPart);

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.isActive()) {
            throw new RuntimeException("Session is no longer active");
        }

        if (!session.getCurrentQrCode().equals(request.getQrCode())) {
            throw new RuntimeException("QR Code has expired or been rotated");
        }

        if (session.getQrExpiryTime().isBefore(LocalDateTime.now())) {
             throw new RuntimeException("QR Code has expired");
        }

        Student student = studentRepository.findByRollNumber(request.getRollNumber())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (attendanceRepository.existsBySessionAndStudent(session, student)) {
            throw new RuntimeException("Attendance already marked for this session");
        }

        Attendance attendance = Attendance.builder()
                .session(session)
                .student(student)
                .markedAt(LocalDateTime.now())
                .status("PRESENT")
                .ipAddress(request.getIpAddress())
                .location(request.getLocation())
                .build();

        attendanceRepository.save(attendance);
    }
}
