package com.qrattendance.service;

import com.qrattendance.model.Attendance;
import com.qrattendance.model.Session;
import com.qrattendance.repository.AttendanceRepository;
import com.qrattendance.repository.CourseRepository;
import com.qrattendance.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;

    public DashboardService(AttendanceRepository attendanceRepository, SessionRepository sessionRepository, CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.sessionRepository = sessionRepository;
        this.courseRepository = courseRepository;
    }

    public Map<String, Object> getOverallStats() {
        long totalStudents = courseRepository.findAll().stream()
                .flatMap(c -> c.getEnrolledStudents().stream())
                .distinct()
                .count();

        long totalAttendances = attendanceRepository.count();

        return Map.of(
            "totalStudents", totalStudents,
            "totalAttendances", totalAttendances,
            "activeSessions", sessionRepository.findByActive(true).size()
        );
    }

    public Map<String, Object> getSessionAnalytics(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        List<Attendance> attendances = attendanceRepository.findBySession(session);

        int presentCount = attendances.size();
        int totalEnrolled = session.getCourse().getEnrolledStudents().size();

        List<Map<String, String>> logs = attendances.stream()
                .map(a -> Map.of(
                    "name", a.getStudent().getName(),
                    "rollNumber", a.getStudent().getRollNumber(),
                    "time", a.getMarkedAt().toString().substring(11, 19),
                    "status", a.getStatus()
                ))
                .collect(Collectors.toList());

        return Map.of(
            "courseName", session.getCourse().getCourseName(),
            "presentCount", presentCount,
            "totalEnrolled", totalEnrolled,
            "attendancePercentage", (totalEnrolled > 0) ? (presentCount * 100.0 / totalEnrolled) : 0,
            "logs", logs
        );
    }
}
