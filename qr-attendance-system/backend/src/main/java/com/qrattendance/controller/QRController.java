package com.qrattendance.controller;

import com.qrattendance.dto.QRResponse;
import com.qrattendance.model.Course;
import com.qrattendance.model.Session;
import com.qrattendance.repository.CourseRepository;
import com.qrattendance.repository.SessionRepository;
import com.qrattendance.service.QRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/qr")
@CrossOrigin(origins = "*") 
public class QRController {

    private final QRService qrService;
    private final CourseRepository courseRepository;
    private final SessionRepository sessionRepository;

    public QRController(QRService qrService, CourseRepository courseRepository, SessionRepository sessionRepository) {
        this.qrService = qrService;
        this.courseRepository = courseRepository;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/generate/{courseId}")
    public ResponseEntity<QRResponse> generateQR(@PathVariable Long courseId) throws Exception {
        return ResponseEntity.ok(qrService.generateAndSaveQR(courseId));
    }

    @GetMapping("/active/{courseId}")
    public ResponseEntity<Map<String, Object>> getActiveSession(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        Session session = sessionRepository.findByCourseAndActive(course, true)
                .stream().findFirst().orElse(null);
        
        if (session != null) {
            return ResponseEntity.ok(Map.of("active", true, "sessionId", session.getId()));
        }
        return ResponseEntity.ok(Map.of("active", false));
    }

    @GetMapping("/current/{sessionId}")
    public ResponseEntity<QRResponse> getQR(@PathVariable Long sessionId) throws Exception {
        return ResponseEntity.ok(qrService.getCurrentSessionQR(sessionId));
    }
}
