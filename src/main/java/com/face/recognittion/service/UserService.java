package com.face.recognittion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.face.recognittion.model.User;
import com.face.recognittion.repository.UserRepository;
import com.face.recognittion.utils.PasswordUtils;
import com.mongodb.DuplicateKeyException;
import com.face.recognittion.model.ClassesDetail;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByStudentId(String studentId) {
        return userRepository.getUserByStudentId(studentId);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User addUser(User user) {
        try {
            user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
            return userRepository.save(user);
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    public boolean verifyUserPassword(String userId, String plainPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        return PasswordUtils.verifyPassword(plainPassword, user.getPassword());
    }

    public List<ClassesDetail> getClassesDetailByTeacherIdAndDate(String teacherId, String date) {
        // Implementation here
        return null;
    }
}
