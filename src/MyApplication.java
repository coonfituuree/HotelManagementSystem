import controllers.*;
import data.PostgreDB;
import data.interfaces.IDB;
import factories.ControllerFactory;
import models.*;
import repositories.*;

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

            int choice = scanner.nextInt();
            scanner.nextLine();

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


    private void adminMainMenu() {
        Map<Integer, Runnable> menuActions = new HashMap<>();
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