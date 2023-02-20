package com.b2b.core.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM");

    public String getTimeRange(LocalTime from, LocalTime to) {// "9:30-10:00"
        return "%s-%s".formatted(from.format(TIME_FORMATTER), to.format(TIME_FORMATTER));
    }

    public String getDateFormat(LocalDate localDate) { //31.01
        return localDate.format(DATE_FORMATTER);
    }
    public static LocalDate isLocalDate(String parameter) {
        try {
            return LocalDate.parse(parameter, DateUtil.DATE_FORMATTER);
        } catch (Exception exception) {
            return null;
        }
    }
    public static LocalDateTime isLocalDateTime(String parameter){
        try {
            return LocalDateTime.parse(parameter);
        } catch (Exception exception) {
            return null;
        }
    }
}
