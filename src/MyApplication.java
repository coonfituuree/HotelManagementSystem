import controllers.*;
import data.PostgreDB;
import models.*;
import repositories.*;

import java.util.List;
import java.util.Scanner;

public class MyApplication {
    private final CustomerController customerController;
    private final RoomController roomController;
    private final BookingController bookingController;
    private final PaymentController paymentController;
    private final AdminController adminController;
    private final Scanner scanner;
    private Customer currentCustomer = null;

    public MyApplication() {
        PostgreDB db = new PostgreDB(
                "localhost",       // Сервер
                "postgres",        // Логин (замени, если у тебя другой)
                "1234",            // Пароль (замени на свой)
                "DBManagementSystem" // Название БД
        );

        this.customerController = new CustomerController(new CustomerRepository(db));
        this.roomController = new RoomController(new RoomRepository(db));
        this.bookingController = new BookingController(new BookingRepository(db));
        this.paymentController = new PaymentController(new PaymentRepository(db));
        this.adminController = new AdminController(new AdminRepository(db));

        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n=== HOTEL MANAGEMENT SYSTEM ===");
            System.out.println("1. Sign up (Регистрация)");
            System.out.println("2. Log in (Вход)");
            System.out.println("0. Exit (Выход)");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void register() {
        System.out.println("\n=== Registration ===");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        Customer customer = new Customer(firstName, lastName, email, password, phone, null);
        String response = customerController.createCustomer(customer);

        if (response.contains("successfully")) {
            System.out.println("Registration successful! Please log in.");
        } else {
            System.out.println("Registration failed! Try again.");
        }
    }

    private void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Customer customer = customerController.getCustomerByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            currentCustomer = customer;
            System.out.println("Login successful! Welcome, " + customer.getFirst_name() + "!");
            customerMainMenu();
        } else {
            System.out.println("Invalid email or password! Try again.");
        }
    }

    private void customerMainMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. View available rooms");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAvailableRooms();
                case 2 -> {
                    currentCustomer = null;
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void showAvailableRooms() {
        System.out.println("\n=== Available Rooms ===");
        List<Room> rooms = roomController.getAllRoomsList();
        if (rooms.isEmpty()) {
            System.out.println("No available rooms at the moment.");
            return;
        }

        for (Room room : rooms) {
            System.out.println(room.getId() + ". " + room.getRoomNumber() + " - " + room.getRoomType() +
                    " - $" + room.getPrice() + " per night");
        }

        System.out.print("Enter room ID to book: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkIn = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOut = scanner.nextLine();

        Booking booking = new Booking(currentCustomer.getId(), roomId, checkIn, checkOut, "booked", null);
        String response = bookingController.createBooking(booking);

        if (response.contains("successfully")) {
            System.out.println("Booking confirmed!");
            processPayment(booking.getId());
        } else {
            System.out.println("Booking failed! Try again.");
        }
    }

    private void processPayment(int bookingId) {
        System.out.println("\n=== Payment ===");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. PayPal");
        System.out.print("Choose a payment method: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String paymentMethod = switch (choice) {
            case 1 -> "cash";
            case 2 -> "credit_card";
            case 3 -> "paypal";
            default -> {
                System.out.println("Invalid option! Using cash by default.");
                yield "cash";
            }
        };

        String response = paymentController.createPayment(bookingId);
        if (response.contains("successfully")) {
            System.out.println("Payment successful! Enjoy your stay.");
        } else {
            System.out.println("Payment failed! Try again.");
        }
    }
}
