package controllers.interfaces;

import models.Booking;

public interface IBookingController {
    String createBooking(Booking booking);
    String getBookingById(int id);
    String getAllBookings();
    String cancelBooking(int id);

    // Добавляем метод уведомления клиента
    void notifyCustomer(int bookingId);
}
