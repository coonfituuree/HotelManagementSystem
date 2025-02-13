import controllers.*;
import data.PostgreDB;
import data.interfaces.IDB;
import factories.ControllerFactory;
import models.*;
import repositories.*;

import utils.DateValidator;
import utils.InputValidator;

import java.sql.SQLException;
import java.util.*;

public class MyApplication {
    private final CustomerController customerController;
    private final RoomController roomController;
    private final BookingController bookingController;
    private final PaymentController paymentController;
    private final AdminController adminController;
    private final Scanner scanner;
    private Customer currentCustomer = null;

    public MyApplication() throws SQLException {
        IDB db = PostgreDB.getInstance();
        ControllerFactory factory = new ControllerFactory(db);

        this.customerController = factory.createCustomerController();
        this.roomController = factory.createRoomController();
        this.bookingController = factory.createBookingController();
        this.paymentController = factory.createPaymentController();
        this.adminController = factory.createAdminController();

        this.scanner = new Scanner(System.in);
    }

    public void run(){
        while(true){
            System.out.println("\n===HOTEL MANAGEMENT SYSTEM===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine();
                continue;
            }

            switch(choice){
                case 1 -> register();
                case 2 -> loginSystem();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    public void register() {
        System.out.println("\n=== Registration ===");

        String firstName;
        do {
            System.out.print("Enter first name: ");
            firstName = scanner.nextLine();
            if (!InputValidator.isValidName(firstName)) {
                System.out.println("Invalid name! Use only letters.");
            }
        } while (!InputValidator.isValidName(firstName));

        String lastName;
        do {
            System.out.print("Enter last name: ");
            lastName = scanner.nextLine();
            if (!InputValidator.isValidName(lastName)) {
                System.out.println("Invalid last name! Use only letters.");
            }
        } while (!InputValidator.isValidName(lastName));

        String email;
        do {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (!InputValidator.isValidEmail(email)) {
                System.out.println("Invalid email format! Example: user@example.com");
            }
        } while (!InputValidator.isValidEmail(email));

        String password;
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (!InputValidator.isValidPassword(password)) {
                System.out.println("Password must be at least 8 characters, contain letters and numbers.");
            }
        } while (!InputValidator.isValidPassword(password));

        String phone;
        do {
            System.out.print("Enter phone number: ");
            phone = scanner.nextLine();
            if (!InputValidator.isValidPhoneNumber(phone)) {
                System.out.println("Invalid phone number! Only digits, at least 10 characters.");
            }
        } while (!InputValidator.isValidPhoneNumber(phone));

        Customer customer = new Customer(firstName, lastName, email, password, phone, null);
        String response = customerController.createCustomer(customer);

        if (response.contains("successfully")) {
            System.out.println("Registration successful! Please log in.");
        } else {
            System.out.println("Registration failed! Please try again.");
        }
    }


    public void loginSystem() {
        System.out.println("\n=== Login System ===");
        System.out.print("Enter email or username: ");
        String identifier = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // CUSTOMER CHECK
        Optional<Customer> customerOpt = Optional.ofNullable(customerController.getCustomerByEmail(identifier));

        if (customerOpt.isPresent() && customerOpt.get().getPassword().equals(password)) {
            currentCustomer = customerOpt.get();
            System.out.println("Login successful! Welcome, " + currentCustomer.getFirst_name() + "!");
            customerMainMenu();
            return;
        }

        //ADMIN CHECK
        Optional<Admin> adminOpt = Optional.ofNullable(adminController.getAdminByUsername(identifier));

        if (adminOpt.isPresent() && adminOpt.get().getPassword().equals(password)) {
            System.out.println("Login successful! Welcome, " + adminOpt.get().getUsername() + "!");
            adminMainMenu();
            return;
        }

        // If no match is found
        System.out.println("Invalid email/username or password! Please try again.");
    }

    private void customerMainMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. View all rooms");
            System.out.println("2. View my bookings");
            System.out.println("3. Make a Payment");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showRooms();
                case 2 -> showMyBookings();
                case 3 -> paymentmenu();
                case 0 -> {
                    currentCustomer = null;
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    private void paymentmenu() {
        System.out.println("\n=== PAYMENT MENU: ===");
        System.out.println("1. Make a Payment");
        System.out.println("2. View My Bookings");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1 -> makeapayment();
            case 2 -> showMyBookings();
            case 0 -> {
                System.out.println("Exiting...");
                return;
            }
            default -> System.out.println("Invalid option! Try again.");
        }
        
    }
    private void makeapayment() {
        System.out.println("\n=== Make a Payment");
        System.out.print("Make a Payment with Booking ID: ");

    }
    private void showRooms() {
        System.out.println("\n=== All Rooms ===");
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

        // Ввод и проверка даты заезда
        String checkIn;
        do {
            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            checkIn = scanner.nextLine();
            if (!DateValidator.isCheckInValid(checkIn)) {
                System.out.println("Invalid check-in date! It must be in format YYYY-MM-DD and not in the past.");
            }
        } while (!DateValidator.isCheckInValid(checkIn));

        // Ввод и проверка даты выезда
        String checkOut;
        do {
            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            checkOut = scanner.nextLine();
            if (!DateValidator.isCheckOutValid(checkIn, checkOut)) {
                System.out.println("Invalid check-out date! It must be after the check-in date.");
            }
        } while (!DateValidator.isCheckOutValid(checkIn, checkOut));

        // Создаем бронь после валидации
        Booking booking = new Booking(currentCustomer.getId(), roomId, checkIn, checkOut, "booked", null);
        String response = bookingController.createBooking(booking);

        if (response.contains("successfully")) {
            System.out.println("Booking confirmed! Please make a payment at our reception using your Booking ID.");
        } else {
            System.out.println("Booking failed! Try again.");
        }
    }



    private void showMyBookings() {
        if (currentCustomer == null) {
            System.out.println("You are not logged in. Please log in first.");
            return;
        }
        System.out.println("\n=== My Bookings ===");
        List<Booking> bookings = bookingController.getBookingsByCustomerId(currentCustomer.getId());
        if (bookings.isEmpty()) {
            System.out.println("You have no bookings.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getId() +
                    ", Room ID: " + booking.getRoomId() +
                    ", Check-in: " + booking.getCheckInDate() +
                    ", Check-out: " + booking.getCheckOutDate() +
                    ", Status: " + booking.getStatus());
        }
    }


    private void adminMainMenu() {
        Map<Integer, Runnable> menuActions = new HashMap<>();
        menuActions.put(1, () -> { showAllBookings(); });
        menuActions.put(2, () -> { cancelBooking(); });
        menuActions.put(3, () -> { showRooms1(); });
        menuActions.put(4, () -> { addRoom(); });
        menuActions.put(5, () -> { deleteRoom(); });
        menuActions.put(0, () -> {
            System.out.println("Logging out...");
        });

        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View all bookings");
            System.out.println("2. Cancel a booking");
            System.out.println("3. View all rooms");
            System.out.println("4. Add a new room");
            System.out.println("5. Delete a room");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            menuActions.getOrDefault(choice, () -> System.out.println("Invalid option! Try again.")).run();


            if (choice == 0) return;
        }
    }

    private void showRooms1() {
        System.out.println("\n=== All Rooms ===");

        Optional.ofNullable(roomController.getAllRoomsList())
                .filter(rooms -> !rooms.isEmpty())
                .ifPresentOrElse(
                        rooms -> rooms.forEach(room ->
                                System.out.println(room.getId() + ". " + room.getRoomNumber() + " - "
                                        + room.getRoomType() + " - $" + room.getPrice() + " per night")),
                        () -> System.out.println("No available rooms at the moment.")
                );
    }

    private void addRoom() {
        System.out.println("\n=== Add New Room ===");
        String roomNumber;
        do {
            System.out.print("Enter room number: ");
            roomNumber = scanner.nextLine();
            if (!InputValidator.isValidRoomNumber(roomNumber)) {
                System.out.println("Invalid room number! Only digits allowed.");
            }
        } while (!InputValidator.isValidRoomNumber(roomNumber));

        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine();

        double price;
        do {
            System.out.print("Enter price per night: ");
            while (!scanner.hasNextDouble()) {
                System.out.println("Invalid price! Must be a positive number.");
                scanner.next();
            }
            price = scanner.nextDouble();
            scanner.nextLine(); // Очищаем буфер ввода
            if (!InputValidator.isValidPrice(price)) {
                System.out.println("Invalid price! Must be greater than 0.");
            }
        } while (!InputValidator.isValidPrice(price));

        System.out.print("Enter status (Available/Booked): ");
        String status = scanner.nextLine();

        Room newRoom = new Room(roomNumber, roomType, price, status, null);
        String response = roomController.createRoom(newRoom);

        if (response.contains("successfully")) {
            System.out.println("Room added successfully!");
        } else {
            System.out.println("Failed to add room. Try again.");
        }
    }
    private void deleteRoom() {
        System.out.println("\n=== Delete Room ===");
        String roomNumber;
        do {
            System.out.print("Enter room number to delete: ");
            roomNumber = scanner.nextLine();
            if (!InputValidator.isValidRoomNumber(roomNumber)) {
                System.out.println("Invalid room number! Only digits allowed.");
            }
        } while (!InputValidator.isValidRoomNumber(roomNumber));

        String response = roomController.deleteRoomByNumber(roomNumber);

        if (response.contains("successfully")) {
            System.out.println("Room deleted successfully!");
        } else {
            System.out.println("Failed to delete room. Room not found or an error occurred.");
        }
    }
    private void showAllBookings() {
        System.out.println("\n=== All Bookings ===");
        List<Booking> bookings = bookingController.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Booking booking : bookings) {
            System.out.println("Booking ID: " + booking.getId() +
                    ", Customer ID: " + booking.getCustomerId() +
                    ", Room ID: " + booking.getRoomId() +
                    ", Check-in: " + booking.getCheckInDate() +
                    ", Check-out: " + booking.getCheckOutDate() +
                    ", Status: " + booking.getStatus());
        }
    }

    private void cancelBooking() {
        System.out.print("Enter booking ID to cancel: ");

        Optional.of(scanner.nextInt())
                .ifPresent(bookingId -> {
                    scanner.nextLine();
                    System.out.println(bookingController.cancelBooking(bookingId));
                });
    }
}