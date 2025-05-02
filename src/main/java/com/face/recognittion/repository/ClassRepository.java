package com.face.recognittion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.face.recognittion.model.Classes;

@Repository
public interface ClassRepository extends MongoRepository<Classes, String> {
    // Add custom query methods if needed
    Classes findByClassCode(String classCode);
}
