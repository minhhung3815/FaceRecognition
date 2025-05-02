package com.face.recognittion.dto;


import lombok.Data;

@Data
public class CheckInDTO {
    private String id;
    private String name;
    private Boolean checkedIn;
}
