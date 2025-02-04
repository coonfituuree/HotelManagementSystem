package controllers.interfaces;

public interface IPaymentController {
    String createPayment(int bookingId);
    String getPaymentById(int id);
    String getAllPayments();
    String updatePaymentStatus(int id, String newStatus);
}
