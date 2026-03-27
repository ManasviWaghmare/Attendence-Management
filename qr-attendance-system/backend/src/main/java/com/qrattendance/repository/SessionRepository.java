package com.qrattendance.repository;

import com.qrattendance.model.Course;
import com.qrattendance.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByCourseAndActive(Course course, boolean active);
    List<Session> findByActive(boolean active);
}
