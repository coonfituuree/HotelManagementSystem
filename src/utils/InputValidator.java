package utils;

import java.util.regex.Pattern;

public class InputValidator {

    // Валидация имени (только буквы, минимум 2 символа)
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-zА-Яа-яЁё]{2,}$");
    }

    // Валидация email
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    // Валидация пароля (минимум 8 символов, хотя бы одна буква и одна цифра)
    public static boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    // Валидация номера телефона (только цифры, минимум 10 символов)
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^\\d{10,15}$");
    }

    // Валидация номера комнаты (только цифры)
    public static boolean isValidRoomNumber(String roomNumber) {
        return roomNumber != null && roomNumber.matches("^\\d+$");
    }

    // Валидация цены комнаты (больше 0)
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidLoginPassword(String password) {
        return password != null && password.length() != 8;
    }
}
