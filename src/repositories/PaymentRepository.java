package repositories;

import data.interfaces.IDB;
import models.Payment;
import repositories.interfaces.IPaymentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IPaymentRepository {
    private final IDB db;

    public PaymentRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createPayment(int bookingId) {
        try (Connection conn = db.getConnection()) {
            // 1. Получаем данные о бронировании и цене номера
            String bookingQuery = "SELECT b.check_in_date, b.check_out_date, r.price " +
                    "FROM bookings b " +
                    "JOIN rooms r ON b.room_id = r.id " +
                    "WHERE b.id = ?";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, bookingId);
            ResultSet rs = bookingStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Booking not found.");
                return false;
            }

            // 2. Рассчитываем количество дней проживания
            Date checkIn = rs.getDate("check_in_date");
            Date checkOut = rs.getDate("check_out_date");
            double pricePerDay = rs.getDouble("price");

            long diff = checkOut.getTime() - checkIn.getTime();
            int days = (int) (diff / (1000 * 60 * 60 * 24)); // Переводим миллисекунды в дни
            double totalAmount = pricePerDay * days;

            // 3. Создаем платеж с рассчитанной суммой
            String sql = "INSERT INTO payments (booking_id, amount, payment_method, status) VALUES (?, ?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, bookingId);
            st.setDouble(2, totalAmount);
            st.setString(3, "pending"); // Метод оплаты указывается позже
            st.setString(4, "pending"); // Платеж ожидает оплаты
            st.execute();

            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Payment getPaymentById(int id) {
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM payments WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM payments");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getString("status"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return payments;
    }

    @Override
    public boolean updatePaymentStatus(int id, String newStatus) {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE payments SET status = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newStatus);
            st.setInt(2, id);

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
}
