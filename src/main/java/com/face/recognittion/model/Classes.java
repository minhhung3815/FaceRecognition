package com.face.recognittion.model;
import lombok.Data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Document(collection = "classes") // MongoDB collection name
@Component
public class Classes {
    @Id
    private String id;

    private String classCode;

    private String course;

    private Integer status;
}

