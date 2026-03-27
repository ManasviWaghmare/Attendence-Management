package com.qrattendance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDateTime markedAt;

    @Column(nullable = false)
    private String status;

    private String ipAddress;
    private String location;

    public Attendance() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public LocalDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(LocalDateTime markedAt) { this.markedAt = markedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @PrePersist
    public void prePersist() {
        if (markedAt == null) markedAt = LocalDateTime.now();
        if (status == null) status = "PRESENT";
    }

    public static AttendanceBuilder builder() { return new AttendanceBuilder(); }
    public static class AttendanceBuilder {
        private Session session;
        private Student student;
        private LocalDateTime markedAt;
        private String status;
        private String ipAddress;
        private String location;
        public AttendanceBuilder session(Session session) { this.session = session; return this; }
        public AttendanceBuilder student(Student student) { this.student = student; return this; }
        public AttendanceBuilder markedAt(LocalDateTime markedAt) { this.markedAt = markedAt; return this; }
        public AttendanceBuilder status(String status) { this.status = status; return this; }
        public AttendanceBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public AttendanceBuilder location(String location) { this.location = location; return this; }
        public Attendance build() {
            Attendance a = new Attendance();
            a.setSession(session);
            a.setStudent(student);
            a.setMarkedAt(markedAt);
            a.setStatus(status);
            a.setIpAddress(ipAddress);
            a.setLocation(location);
            return a;
        }
    }
}
