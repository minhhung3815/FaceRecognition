package com.face.recognittion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component; 

@Data
@Component
@Document(collection = "users") // MongoDB collection name
public class User {
    @Id
    private String id;

    private String name;

    private String studentId;

    private String password;

    private Integer role;
}

