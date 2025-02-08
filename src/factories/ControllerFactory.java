package factories;

import controllers.*;
import repositories.*;
import data.interfaces.IDB;

public class ControllerFactory {
    private final IDB db;

    public ControllerFactory(IDB db) {
        this.db = db;
    }

    public CustomerController createCustomerController() {
        return new CustomerController(new CustomerRepository(db));
    }

    public RoomController createRoomController() {
        return new RoomController(new RoomRepository(db));
    }

    public BookingController createBookingController() {
        return new BookingController(new BookingRepository(db));
    }

    public PaymentController createPaymentController() {
        return new PaymentController(new PaymentRepository(db));
    }

    public AdminController createAdminController() {
        return new AdminController(new AdminRepository(db));
    }
}
