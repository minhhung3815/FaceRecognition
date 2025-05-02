package com.face.recognittion.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.face.recognittion.model.ClassesDetail;

import java.util.List;

@Repository
public interface ClassesDetailRepository extends MongoRepository<ClassesDetail, String> {
    // Add custom query methods if needed
    ClassesDetail findByClassId(String classId);
    List<ClassesDetail> findByTeacherIdAndDateAndStatus(String teacherId, String date, Integer status);
    
}