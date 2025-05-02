package com.face.recognittion.dto;

import lombok.Data;

@Data
public class AttendanceResponseDTO {
    private String id;
    private String name;
    private String code;
    private String time;
    private Boolean isCheckin;
}
