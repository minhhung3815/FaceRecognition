package com.face.recognittion.dto;

import lombok.Data;

@Data
public class ImageRequest {
    private String image;
    private String classDetailId;
    private String studentId;
    private String date;
}
