package com.face.recognittion.repository;

import com.face.recognittion.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Add custom query methods if needed
    User getUserByStudentId(String studentId);
    
}
