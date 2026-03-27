package com.qrattendance.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String currentQrCode;

    private LocalDateTime qrExpiryTime;

    @Column(nullable = false)
    private boolean active = true;

    public Session() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getCurrentQrCode() { return currentQrCode; }
    public void setCurrentQrCode(String currentQrCode) { this.currentQrCode = currentQrCode; }
    public LocalDateTime getQrExpiryTime() { return qrExpiryTime; }
    public void setQrExpiryTime(LocalDateTime qrExpiryTime) { this.qrExpiryTime = qrExpiryTime; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static SessionBuilder builder() { return new SessionBuilder(); }
    public static class SessionBuilder {
        private Course course;
        private LocalDateTime startTime;
        private boolean active = true;
        public SessionBuilder course(Course course) { this.course = course; return this; }
        public SessionBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public SessionBuilder active(boolean active) { this.active = active; return this; }
        public Session build() { 
            Session s = new Session();
            s.setCourse(course);
            s.setStartTime(startTime);
            s.setActive(active);
            return s;
        }
    }
}