package com.face.recognittion.dto;

import org.springframework.stereotype.Component;

import com.face.recognittion.model.ClassesDetail;

import lombok.Data;

@Data
@Component
public class AttendanceDTO {
    private String id;

    private String classDetailId; // Reference to the class detail

    private String studentId; // Reference to the student

    private String date; // Date of the class session

    private boolean checkIn; // Check-in status (default is false)

    private ClassesDetail classesDetail; // Reference to the class detail
}
