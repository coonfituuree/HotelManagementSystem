package repositories;

import data.interfaces.IDB;
import models.Admin;
import repositories.interfaces.IAdminRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository implements IAdminRepository {
    private final IDB db;

    public AdminRepository(IDB db) {
        this.db = db;
    }

    @Override
    public boolean createAdmin(Admin admin) {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO admins (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, admin.getUsername());
            st.setString(2, admin.getPassword()); // В реальном проекте здесь должен быть хеш пароля
            st.setString(3, admin.getRole());
            st.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Admin getAdminByUsername(String username) {
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM admins WHERE username = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM admins");
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return admins;
    }

    @Override
    public boolean updateAdminRole(int id, String newRole) {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE admins SET role = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, newRole);
            st.setInt(2, id);

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAdmin(int id) {
        try (Connection conn = db.getConnection()) {
            String sql = "DELETE FROM admins WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);

            int rowsDeleted = st.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
}
