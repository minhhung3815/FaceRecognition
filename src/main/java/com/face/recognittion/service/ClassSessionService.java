package com.face.recognittion.service;

import com.face.recognittion.model.ClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.face.recognittion.repository.ClassSessionRepository;

@Service
public class ClassSessionService {

    @Autowired
    private ClassSessionRepository classSessionRepository;

    public void addClassSession(String classDetailId, String studentId) {
        boolean exists = classSessionRepository.existsByClassDetailIdAndStudentId(classDetailId, studentId);
        if (exists) {
            throw new IllegalArgumentException("Student already exists in this class");
        }

        ClassSession classSession = new ClassSession();
        classSession.setClassDetailId(classDetailId);
        classSession.setStudentId(studentId);
        classSessionRepository.save(classSession);
    }
}