package com.face.recognittion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Component
@Document(collection = "attendance") // MongoDB collection name
public class Attendance {
    @Id
    private String id;

    private String classDetailId; // Reference to the class detail

    private String studentId; // Reference to the student

    private String date; // Date of the class session

    private boolean checkIn; // Check-in status (default is false)
}
