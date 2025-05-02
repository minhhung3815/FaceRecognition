package com.face.recognittion.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.face.recognittion.model.User;
import com.face.recognittion.model.Classes;
import com.face.recognittion.model.ClassesDetail;
import com.face.recognittion.requests.AssignStudentRequest;
import com.face.recognittion.requests.ClassRequest;
import com.face.recognittion.requests.UserRequest;
import com.face.recognittion.service.ClassSessionService;
import com.face.recognittion.service.ClassesService;
import com.face.recognittion.service.UserService;
import com.face.recognittion.service.AttendanceService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import com.face.recognittion.config.UserEnum;

import com.face.recognittion.config.Constants;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService studentService;

    @Autowired
    private ClassesService classesService;

    @Autowired
    private ClassSessionService classSessionService;

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest param) {
        try {
            String name = param.getName();
            String password = param.getPassword();
            Integer role = param.getRole();
            String studentId = param.getStudentId();

            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body("Name is required");
            }
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }
            if (role == null) {
                return ResponseEntity.badRequest().body("Role is required");
            }

            User user = new User();
            user.setName(name);
            user.setPassword(password);
            user.setRole(role);
            user.setStudentId(studentId);

            if (role != null && role.equals(UserEnum.ROLE_STUDENT.getValue())) {
                User findUser = studentService.getUserByStudentId(studentId);
                if (findUser != null && findUser.getRole().equals(UserEnum.ROLE_STUDENT.getValue())) {
                    return ResponseEntity.badRequest().body("User already exists");
                }
            }
                
            User addUser = studentService.addUser(user);

            if (addUser == null) {
                return ResponseEntity.badRequest().body("Failed to create user");
            }

            return ResponseEntity.ok(addUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
        
    }

    @PostMapping("/create-classes")
    public ResponseEntity<?> createClasses(@RequestBody ClassRequest entity) {
        try {
            String classCode = entity.getClassCode();

            if (classCode == null || classCode.isEmpty()) {
                return ResponseEntity.badRequest().body("Class name is required");
            }
            Classes newClass = null;

            newClass = classesService.getClassByClassCode(classCode);
            if (newClass == null) {
                newClass = classesService.createClass(classCode);
            }
            if (newClass == null) {
                return ResponseEntity.badRequest().body("Failed to create class");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String classId = newClass.getId();
            String semeterId = entity.getSemesterId();
            String date = entity.getDate();
            String startTime = entity.getStartTime();
            String endTime = entity.getEndTime();
            Integer noWeeks = entity.getNoWeeks();
            String startClassTime = entity.getStartClassTime();
            String endClassTime = entity.getEndClassTime();
            String teacherId = entity.getTeacherId();
            String course = entity.getCourse();

            LocalDate startTimeParsedDate = LocalDate.parse(startTime, formatter);
            LocalDate endTimeParsedDate = LocalDate.parse(endTime, formatter);
            LocalDateTime startTimeParsed = startTimeParsedDate.atStartOfDay(); // Convert to LocalDateTime
            LocalDateTime endTimeParsed = endTimeParsedDate.atStartOfDay(); // Convert to LocalDateTime
            if (semeterId == null || semeterId.isEmpty()) {
                return ResponseEntity.badRequest().body("Semester ID is required");
            }
            if (date == null || date.isEmpty()) {
                return ResponseEntity.badRequest().body("Date is required");
            }
            if (course == null || course.isEmpty()) {
                return ResponseEntity.badRequest().body("Course name is required");
            }
            if (startTime == null) {
                return ResponseEntity.badRequest().body("Start time is required");
            }
            if (endTime == null) {
                return ResponseEntity.badRequest().body("End time is required");
            }
            if (noWeeks == null) {
                return ResponseEntity.badRequest().body("Number of weeks is required");
            }
            if (startClassTime == null) {
                return ResponseEntity.badRequest().body("Start class time is required");
            }
            if (endClassTime == null) {
                return ResponseEntity.badRequest().body("End class time is required");
            }
            if (teacherId == null || teacherId.isEmpty()) {
                return ResponseEntity.badRequest().body("Teacher ID is required");
            }

            ClassesDetail classesDetail = classesService.createClassesDetail(teacherId, semeterId, date, startTimeParsed, endTimeParsed, noWeeks, startClassTime, endClassTime, classId, course, classCode);

            if (classesDetail == null) {
                return ResponseEntity.badRequest().body("Failed to create classes detail");
            }
            return ResponseEntity.ok(classesDetail);
            // Logic to create classes
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating classes: " + e.getMessage());
        }
        
    }

    @PostMapping("/assign-student") 
    public ResponseEntity<?> assignStudents(@RequestBody AssignStudentRequest entity) {
        try {
            String classDetailId = entity.getClassDetailId();
            String studentId = entity.getStudentId();

            if (classDetailId == null || classDetailId.isEmpty()) {
                return ResponseEntity.badRequest().body("Class detail ID is required");
            }
            if (studentId == null || studentId.isEmpty()) {
                return ResponseEntity.badRequest().body("Student ID is required");
            }

            Optional<ClassesDetail> classesDetailOptional = classesService.getClassesDetailById(classDetailId);
            if (!classesDetailOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Class detail not found");
            }
            ClassesDetail classesDetail = classesDetailOptional.get();

            if (!classesDetail.getStatus().equals(Constants.ACTIVE_STATUS)) {
                return ResponseEntity.badRequest().body("Class detail is not active");
            }

            // Calculate the start date based on the day of the week
            LocalDate startDate = calculateStartDate(classesDetail);

            int noWeeks = classesDetail.getNoWeeks();

            // Assign student to the class
            classSessionService.addClassSession(classDetailId, studentId);

            // Create attendance data
            attendanceService.createAttendance(classDetailId, studentId, startDate, noWeeks);

            return ResponseEntity.ok("Student assigned successfully and attendance created");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error assigning students: " + e.getMessage());
        }
    }

    private LocalDate calculateStartDate(ClassesDetail classesDetail) {
        // Get the day of the week as an integer (e.g., Monday = 2, Sunday = 8)
        int targetDayOfWeek = Integer.parseInt(classesDetail.getDate());

        // Get the start date from the class detail's start time
        LocalDate startDate = classesDetail.getStartTime().toLocalDate();

        // Adjust the start date to the first occurrence of the target day of the week
        while (startDate.getDayOfWeek().getValue() != targetDayOfWeek) {
            startDate = startDate.plusDays(1);
        }

        return startDate;
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestParam String userId, @RequestParam String plainPassword) {
        boolean isValid = studentService.verifyUserPassword(userId, plainPassword);
        if (!isValid) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        return ResponseEntity.ok("Password is valid");
    }

    @GetMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam String param) {
        return ResponseEntity.ok(true);
    }
    @GetMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestParam String param) {
        return ResponseEntity.ok(true);
    }
}
