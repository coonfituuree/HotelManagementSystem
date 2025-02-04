package repositories;

import data.interfaces.IDB;
import models.Customer;
import repositories.interfaces.ICustomerRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {
    private final IDB db;

    public CustomerRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createCustomer(Customer customer) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "INSERT INTO customers (first_name, last_name, email, password, phone_number) VALUES (?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);

            st.setString(1, customer.getFirst_name());
            st.setString(2, customer.getLast_name());
            st.setString(3, customer.getEmail());
            st.setString(4, customer.getPassword());
            st.setString(5, customer.getPhone_number());

            st.execute();
            return true;
        } catch (SQLException e){
            System.out.println("Sql Error: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Customer getCustomerById(int id) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "SELECT * FROM customers WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("customer_id"));
            }
        } catch (SQLException e){
            System.out.println("Sql Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM customers");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                customers.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM customers WHERE email = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_number"),
                        rs.getString("created_at")
                );
            } else {
                System.out.println("DEBUG: No customer found in DB with email: " + email);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }

}
