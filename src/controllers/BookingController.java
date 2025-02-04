package controllers;

import controllers.interfaces.IBookingController;
import models.Booking;
import repositories.interfaces.IBookingRepository;

import java.util.List;

public class BookingController implements IBookingController {
    private final IBookingRepository repository;

    public BookingController(IBookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public String createBooking(Booking booking) {
        boolean created = repository.createBooking(booking);
        return created ? "Booking successfully created!" : "Failed to create booking. Room might be unavailable.";
    }

    @Override
    public String getBookingById(int id) {
        Booking booking = repository.getBookingById(id);
        return booking != null ? booking.toString() : "Booking not found.";
    }

    @Override
    public String getAllBookings() {
        List<Booking> bookings = repository.getAllBookings();
        return bookings.isEmpty() ? "No bookings found." : bookings.toString();
    }

    @Override
    public String cancelBooking(int id) {
        boolean canceled = repository.cancelBooking(id);
        return canceled ? "Booking canceled successfully!" : "Failed to cancel booking. Check the booking ID.";
    }
    @Override
    public void notifyCustomer(int bookingId) {
        Booking booking = repository.getBookingById(bookingId);
        if (booking != null) {
            System.out.println("📢 Уведомление: Ваше бронирование #" + booking.getId() +
                    " на " + booking.getCheckInDate() + " - " + booking.getCheckOutDate() +
                    " успешно создано!");
        }
    }

}
