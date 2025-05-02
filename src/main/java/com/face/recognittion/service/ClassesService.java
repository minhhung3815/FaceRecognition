package com.face.recognittion.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import com.face.recognittion.model.Attendance;
import com.face.recognittion.model.Classes;
import com.face.recognittion.model.ClassesDetail;
import com.face.recognittion.repository.AttendanceRepository;
import com.face.recognittion.repository.ClassRepository;
import com.face.recognittion.repository.ClassesDetailRepository;
import com.face.recognittion.config.Constants;

@Service
public class ClassesService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassesDetailRepository classesDetailRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private Classes classes;

    @Autowired
    private ObjectProvider<ClassesDetail> classesDetailProvider;
    
    public Classes createClass(String classCode) {
        classes.setClassCode(classCode);
        classes.setStatus(Constants.ACTIVE_STATUS);
        Classes newClass = classRepository.save(classes);
        return newClass;
    }

    public Classes getClassByClassCode(String classCode) {
        return classRepository.findByClassCode(classCode);
    }

    public Optional<ClassesDetail> getClassesDetailById(String classDetailId) {
        return classesDetailRepository.findById(classDetailId);
    }
    public ClassesDetail createClassesDetail(String teacherId, String semeterId, String date, LocalDateTime startTime,
            LocalDateTime endTime, Integer noWeeks, String startClassTime, String endClassTime,
            String classId, String course, String classCode) {
                try {
                    ClassesDetail classesDetail = classesDetailProvider.getObject();
                    classesDetail.setTeacherId(teacherId);
                    classesDetail.setClassId(classId);
                    classesDetail.setSemesterId(semeterId);
                    classesDetail.setDate(date);
                    classesDetail.setStartTime(startTime);
                    classesDetail.setEndTime(endTime);
                    classesDetail.setNoWeeks(noWeeks);
                    classesDetail.setStartClassTime(startClassTime);
                    classesDetail.setEndClassTime(endClassTime);
                    classesDetail.setStatus(Constants.ACTIVE_STATUS);
                    classesDetail.setCourse(course);
                    classesDetail.setClassCode(classCode);
                    ClassesDetail newClassesDetail = classesDetailRepository.insert(classesDetail);
                    return newClassesDetail;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;    
                } 
    }

    public List<ClassesDetail> getClassesDetailByTeacherIdAndDate(String teacherId, String date) {
        return classesDetailRepository.findByTeacherIdAndDateAndStatus(teacherId, date, Constants.ACTIVE_STATUS);
    }

    public List<Attendance> getTotalStudentsByClassDetailIdAndDate(String classDetailId, String date) {
        return attendanceRepository.getTotalStudentsByClassDetailIdAndDate(classDetailId, date);
    }
}
