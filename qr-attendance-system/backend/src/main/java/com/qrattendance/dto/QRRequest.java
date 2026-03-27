package com.qrattendance.dto;

public class QRRequest {
    private Long courseId;

    public QRRequest() {}
    public QRRequest(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
}
