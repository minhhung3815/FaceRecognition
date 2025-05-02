package com.face.recognittion.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class DateUtil {

    public static int getDayOfWeek(String date) {
        // Parse the date string into a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Get the day of the week
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        // Map the day of the week to the desired number
        switch (dayOfWeek) {
            case MONDAY:
                return 2;
            case TUESDAY:
                return 3;
            case WEDNESDAY:
                return 4;
            case THURSDAY:
                return 5;
            case FRIDAY:
                return 6;
            case SATURDAY:
                return 7;
            case SUNDAY:
                return 8;
            default:
                throw new IllegalArgumentException("Invalid day of the week");
        }
    }
}
