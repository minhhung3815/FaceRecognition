package com.face.recognittion.repository;

import java.util.List;
import java.util.Map;

public interface AttendanceRepositoryCustom {
    List<Map<String, Object>> findAttendanceWithClassDetails(String studentId, String date);
}
