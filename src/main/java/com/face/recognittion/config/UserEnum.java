package com.face.recognittion.config;

public enum UserEnum {
    
    // Constants for user roles
    ROLE_ADMIN(1),
    ROLE_STUDENT(2),
    ROLE_TEACHER(3),

    // Constants for user status
    STATUS_ACTIVE(1),
    STATUS_INACTIVE(0);

    private final int value;

    UserEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
