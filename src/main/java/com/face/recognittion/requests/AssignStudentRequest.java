package com.face.recognittion.requests;

import lombok.Data;

@Data
public class AssignStudentRequest {
    String studentId;
    String classDetailId;
    Integer status;
}
