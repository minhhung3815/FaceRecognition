package com.face.recognittion.requests;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClassRequest {
    private String classId;

    private String classCode;

    private String teacherId; // Reference to the teacher (from User model)

    private String studentId; // Reference to the students (from User model)

    private String semesterId; // Reference to the semester

    private String date; // Date the class will take place

    private String startTime; // Start time of the session

    private String endTime; // End time of the session

    private Integer noWeeks;

    private String startClassTime; // Start time of the session

    private String endClassTime; // End time of the session

    private String course;

    private Integer status;
    
}
