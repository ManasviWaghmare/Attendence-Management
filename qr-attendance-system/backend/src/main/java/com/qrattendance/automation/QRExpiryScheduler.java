package com.qrattendance.automation;

import com.qrattendance.model.Session;
import com.qrattendance.repository.SessionRepository;
import com.qrattendance.util.QRValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QRExpiryScheduler {
    private static final Logger log = LoggerFactory.getLogger(QRExpiryScheduler.class);

    private final SessionRepository sessionRepository;
    private final QRValidator qrValidator;

    public QRExpiryScheduler(SessionRepository sessionRepository, QRValidator qrValidator) {
        this.sessionRepository = sessionRepository;
        this.qrValidator = qrValidator;
    }

    @Scheduled(fixedRate = 10000)
    public void rotateQRCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<Session> activeSessions = sessionRepository.findByActive(true);

        for (Session session : activeSessions) {
            if (session.getQrExpiryTime() == null || session.getQrExpiryTime().isBefore(now)) {
                log.info("Rotating QR for session ID: {}", session.getId());
                
                String rawToken = String.format("session:%d|time:%s|nonce:%s",
                        session.getId(),
                        now.toString(),
                        UUID.randomUUID().toString());

                String encryptedToken = qrValidator.encrypt(rawToken);

                session.setCurrentQrCode(encryptedToken);
                session.setQrExpiryTime(now.plusMinutes(5));
                sessionRepository.save(session);
            }
        }
    }
}
