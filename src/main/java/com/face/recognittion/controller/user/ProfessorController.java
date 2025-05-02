package com.face.recognittion.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.face.recognittion.dto.CheckInDTO;
import com.face.recognittion.dto.ClassesDTO;
import com.face.recognittion.model.Attendance;
import com.face.recognittion.model.ClassesDetail;
import com.face.recognittion.model.User;
import com.face.recognittion.service.ClassesService;
import com.face.recognittion.service.UserService;
import com.face.recognittion.util.DateUtil;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    UserService studentService;

    @Autowired
    private ClassesService classesService;
    
    @GetMapping("/classes")
    public ResponseEntity<?> getListClasses(@RequestParam String param) {
        return ResponseEntity.ok(true);
    }

    @GetMapping("/students/check-in")
    public ResponseEntity<?> checkInStudent(@RequestParam(required = false) Map<String,String> param) {
        User user = studentService.getUserByStudentId(param.get("studentId"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/classes/details")
    public ResponseEntity<?> getClassDetails(@RequestBody Map<String, Object> request) {
        try {
            String date = (String) request.get("date");
            String teacherId = (String) request.get("teacherId");

            String no_date = String.valueOf(DateUtil.getDayOfWeek(date));

            // Fetch classesDetail by teacherId, date, and status = 1
            List<ClassesDetail> classesDetails = classesService.getClassesDetailByTeacherIdAndDate(teacherId, no_date);

            if (classesDetails == null || classesDetails.isEmpty()) {
                return ResponseEntity.ok("No classes found for the given date and teacher ID.");
            }
            List<ClassesDTO> listClassesDTO = new ArrayList<>();
            for (ClassesDetail classesDetail : classesDetails) {
                // Convert the date to a more readable format if needed
                ClassesDTO classesDTO = new ClassesDTO();
                classesDTO.setId(classesDetail.getId());
                classesDTO.setStartTime(classesDetail.getStartClassTime());
                classesDTO.setEndTime(classesDetail.getEndClassTime());
                classesDTO.setTitle(classesDetail.getCourse());
                classesDTO.setDate(date);
                
                List<Attendance> totalStudents = classesService.getTotalStudentsByClassDetailIdAndDate(classesDetail.getId(), date);

                classesDTO.setStudents(totalStudents.size());
                Integer no_checkIn = 0;
                for (Attendance attendance : totalStudents) {
                    if (attendance.isCheckIn()) {
                        no_checkIn++;
                    }
                    User student = studentService.getUserById(attendance.getStudentId());
                    if (student != null) {
                        CheckInDTO studentDTO = new CheckInDTO();
                        studentDTO.setId(student.getId());
                        studentDTO.setCheckedIn(attendance.isCheckIn());
                        studentDTO.setName(student.getName());
                        classesDTO.getListStudent().add(studentDTO);
                    }
                }
                classesDTO.setCheckedIn(no_checkIn);
                listClassesDTO.add(classesDTO);
            }

            return ResponseEntity.ok(listClassesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch class details: " + e.getMessage());
        }
    }
    
}
