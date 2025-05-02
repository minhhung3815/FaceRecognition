package com.face.recognittion.requests;

import lombok.Data;

@Data
public class UserRequest {
    String name;
    String studentId;
    String password;
    Integer role;
}
