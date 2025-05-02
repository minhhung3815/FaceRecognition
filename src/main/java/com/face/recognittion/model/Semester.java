package com.face.recognittion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Component
@Document(collection = "semesters") // MongoDB collection name
public class Semester {
    @Id
    private String id;

    private String name; // Name of the semester (e.g., "Spring 2023")

    private int year; // Year of the semester

    private boolean status; // True if the semester is active, false if finished
}
