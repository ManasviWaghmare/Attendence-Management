package com.qrattendance.repository;

import com.qrattendance.model.Attendance;
import com.qrattendance.model.Session;
import com.qrattendance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findBySession(Session session);
    List<Attendance> findByStudent(Student student);
    Optional<Attendance> findBySessionAndStudent(Session session, Student student);
    boolean existsBySessionAndStudent(Session session, Student student);
}
