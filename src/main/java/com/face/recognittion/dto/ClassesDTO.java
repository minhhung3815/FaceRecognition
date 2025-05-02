package com.face.recognittion.dto;

import java.util.ArrayList;
import java.util.List;

import com.face.recognittion.model.User;

import lombok.Data;

@Data
public class ClassesDTO {
    String id;
    String title;
    String date;
    String startTime;
    String endTime;
    Integer checkedIn; // 0: not checked in, 1: checked in
    List<CheckInDTO> listStudent = new ArrayList<>(); // list of students in the class
    Integer students; // number of students in the class
}
