import controllers.*;
import data.PostgreDB;
import models.*;
import repositories.*;

import java.util.*;

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

    public void run(){
        while(true){
            System.out.println("\n===HOTEL MANAGEMENT SYSTEM===");
            System.out.println("1. Register");
            System.out.println("2. Login as Customer");
            System.out.println("3. Login as Administrator");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1 -> register();
                case 2 -> loginCustomer();
                case 3 -> loginAdministrator();
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
    }

    public void register(){
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

        Customer customer = new Customer(firstName, lastName, email, password,phone,null);
        String response = customerController.createCustomer(customer);

        if(response.contains("successfully")){
            System.out.println("Registration successful! Please log in.");
        } else {
            System.out.println("Registration failed! Please try again.");
        }
    }

    public void loginCustomer() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional.ofNullable(customerController.getCustomerByEmail(email))
                .filter(customer -> customer.getPassword().equals(password))
                .ifPresentOrElse(
                        customer -> {
                            currentCustomer = customer;
                            System.out.println("Login successful! Welcome, " + customer.getFirst_name() + "!");
                            customerMainMenu();
                        },
                        () -> System.out.println("Invalid email or password! Please try again.")
                );
    }


    private void customerMainMenu() {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. View all rooms");
            System.out.println("2. View my bookings");
            System.out.println("0. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showRooms();
                case 2 -> showMyBookings();
                case 0 -> {
                    currentCustomer = null;
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option! Try again.");
            }
        }
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

        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        String checkIn = scanner.nextLine();
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        String checkOut = scanner.nextLine();

        Booking booking = new Booking(currentCustomer.getId(), roomId, checkIn, checkOut, "booked", null);
        String response = bookingController.createBooking(booking);

        if (response.contains("successfully")) {
            System.out.println("Booking confirmed! Please make a payment at our reseption using your Booking ID, which you can find in 'View My Bookings");
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

    private void loginAdministrator() {
        System.out.println("\n=== Administrator Login ===");
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password (min. 128 digits): ");
        String password = scanner.nextLine();

        Optional.ofNullable(adminController.getAdminByUsername(username))
                .filter(admin -> Objects.equals(admin.getPassword(), password)) // Проверка пароля
                .ifPresentOrElse(
                        admin -> {
                            System.out.println("Login successful! Welcome, " + admin.getUsername() + "!");
                            adminMainMenu();
                        },
                        () -> System.out.println("Invalid username or password! Please try again.")
                );
    }


    private void adminMainMenu() {
        Map<Integer, Runnable> menuActions = new HashMap<>();

        // Полные лямбда-выражения
        menuActions.put(1, () -> { showAllBookings(); });
        menuActions.put(2, () -> { cancelBooking(); });
        menuActions.put(3, () -> { showRooms1(); });
        menuActions.put(4, () -> { addRoom(); });
        menuActions.put(0, () -> {
            System.out.println("Logging out...");
        });

        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View all bookings");
            System.out.println("2. Cancel a booking");
            System.out.println("3. View all rooms");
            System.out.println("4. Add a new room");
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
        System.out.print("Enter room number: ");
        String roomNumber = scanner.nextLine();

        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine();

        System.out.print("Enter price per night: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

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