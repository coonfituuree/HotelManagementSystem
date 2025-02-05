package repositories.interfaces;

import models.Booking;
import java.util.List;

public interface IBookingRepository {
    boolean createBooking(Booking booking);
    int createBooking1(Booking booking);
    Booking getBookingById(int id);
    List<Booking> getAllBookings();
    boolean cancelBooking(int id);
    boolean isRoomAvailable(int roomId, String checkInDate, String checkOutDate);
    void cancelUnpaidBookings();
    List<Booking> getBookingsByCustomerId(int customerId);
}
