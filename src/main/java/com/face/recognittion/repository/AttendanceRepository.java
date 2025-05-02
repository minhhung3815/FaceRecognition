package com.face.recognittion.repository;

import com.face.recognittion.dto.AttendanceDTO;
import com.face.recognittion.model.Attendance;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<AttendanceDTO> findByStudentIdAndDate(String studentId, String date);
    List<Attendance> getTotalStudentsByClassDetailIdAndDate(String classDetailId, String date);
    Attendance findByStudentIdAndClassDetailIdAndDate(String studentId, String classDetailId, String date);
}

