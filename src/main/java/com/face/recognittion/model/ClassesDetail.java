package com.face.recognittion.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;

@Data
@Component
@Scope("prototype")
@Document(collection = "classes_detail") // MongoDB collection name
public class ClassesDetail {
    @Id
    private String id;

    private String classId;

    private String teacherId; // Reference to the teacher (from User model)

    private String semesterId; // Reference to the semester

    private String course;

    private String classCode;

    private String date; // Date the class will take place

    private LocalDateTime startTime; // Start time of the session

    private LocalDateTime endTime; // End time of the session

    private Integer noWeeks;

    private String startClassTime; // Start time of the session

    private String endClassTime; // End time of the session

    private Integer status;

    private List<Attendance> listStudent; // List of students in the class
}
