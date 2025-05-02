package com.face.recognittion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Component
@Document(collection = "class_sessions") // MongoDB collection name
public class ClassSession {
    @Id
    private String id;

    private String classDetailId; // Reference to the class

    private String studentId; // Reference to the class
}
