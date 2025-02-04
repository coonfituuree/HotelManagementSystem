package repositories.interfaces;

import models.Booking;
import java.util.List;

public interface IBookingRepository {
    boolean createBooking(Booking booking);
    Booking getBookingById(int id);
    List<Booking> getAllBookings();
    boolean cancelBooking(int id);
    boolean isRoomAvailable(int roomId, String checkInDate, String checkOutDate);

    // Добавляем метод авто-отмены бронирований
    void cancelUnpaidBookings();
}
