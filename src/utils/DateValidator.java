package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isCheckInValid(String checkInStr) {
        if (!isValidDate(checkInStr)) return false;
        LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
        return !checkIn.isBefore(LocalDate.now());
    }

    public static boolean isCheckOutValid(String checkInStr, String checkOutStr) {
        if (!isValidDate(checkInStr) || !isValidDate(checkOutStr)) return false;
        LocalDate checkIn = LocalDate.parse(checkInStr, formatter);
        LocalDate checkOut = LocalDate.parse(checkOutStr, formatter);
        return checkOut.isAfter(checkIn);
    }
}
