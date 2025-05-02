package com.face.recognittion.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.face.recognittion.dto.AttendanceDTO;
import com.face.recognittion.dto.AttendanceResponseDTO;
import com.face.recognittion.dto.StudentClassesRequest;
import com.face.recognittion.model.Attendance;
import com.face.recognittion.model.ClassesDetail;
import com.face.recognittion.service.AttendanceService;
import com.face.recognittion.service.ClassesService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ClassesService classesService;

    @PostMapping("/classes")
    public ResponseEntity<?> getStudentClasses(@RequestBody StudentClassesRequest request) {
        try {
            String studentId = request.getStudentId();
            String date = request.getDate();

            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.badRequest().body("Student ID is required");
            }
            if (date == null || date.isEmpty()) {
                return ResponseEntity.badRequest().body("Date is required");
            }

            // Fetch attendance with class details using $lookup
            List<AttendanceDTO> result = attendanceService.getAttendanceByStudentIdAndDate(studentId, date);

            for (AttendanceDTO attendance : result) {
                Optional<ClassesDetail> classDetail = classesService.getClassesDetailById(attendance.getClassDetailId());
                if (classDetail.isPresent()) {
                    attendance.setClassesDetail(classDetail.get());
                } else {
                    attendance.setClassesDetail(new ClassesDetail());
                }
            }

            List<AttendanceResponseDTO> attendanceResponseList = new ArrayList<>();
            for (AttendanceDTO attendance : result) {
                AttendanceResponseDTO response = new AttendanceResponseDTO();
                response.setId(attendance.getId());
                response.setName(attendance.getClassesDetail().getCourse());
                response.setIsCheckin(attendance.isCheckIn());
                response.setCode(attendance.getClassesDetail().getClassCode());
                response.setTime(attendance.getClassesDetail().getStartClassTime());
                attendanceResponseList.add(response);
            }

            return ResponseEntity.ok(attendanceResponseList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/is-checked-in")
    public ResponseEntity<?> isStudentCheckedIn(@RequestParam String param) {
        // Logic to check if student is checked in
        return ResponseEntity.ok(true);
    }
}
