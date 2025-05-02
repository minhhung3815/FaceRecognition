package com.face.recognittion.service;

import com.face.recognittion.dto.AttendanceDTO;
import com.face.recognittion.model.Attendance;
import com.face.recognittion.repository.AttendanceRepository;
import com.face.recognittion.repository.AttendanceRepositoryCustom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceRepositoryCustom attendanceRepositoryCustom;

    public void createAttendance(String classDetailId, String studentId, LocalDate startDate, int noWeeks) {
        List<Attendance> attendanceList = new ArrayList<>();
        for (int i = 0; i < noWeeks; i++) {
            Attendance attendance = new Attendance();
            attendance.setClassDetailId(classDetailId);
            attendance.setStudentId(studentId);
            attendance.setDate(startDate.plusWeeks(i).toString()); // Increment date by weeks
            attendance.setCheckIn(false); // Default check-in status
            attendanceList.add(attendance);
        }
        attendanceRepository.saveAll(attendanceList);
    }

    public List<AttendanceDTO> getAttendanceByStudentIdAndDate(String studentId, String date) {
        return attendanceRepository.findByStudentIdAndDate(studentId, date);
        
    }

    public void updateCheckIn(String studentId, String classDetailId, String date) {
        Attendance attendance = attendanceRepository.findByStudentIdAndClassDetailIdAndDate(studentId, classDetailId, date);
        if (attendance != null) {
            attendance.setCheckIn(true);
            attendanceRepository.save(attendance);
        }
    }
}
