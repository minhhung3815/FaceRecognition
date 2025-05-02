package com.face.recognittion.repository;

import com.face.recognittion.model.ClassSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassSessionRepository extends MongoRepository<ClassSession, String> {
    boolean existsByClassDetailIdAndStudentId(String classDetailId, String studentId);
}