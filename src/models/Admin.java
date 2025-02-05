package models;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String role;
    private String createdAt;

    // Конструкторы
    public Admin(int id, String firstName, String lastName, String email, String password, String role) {}

    public Admin(String username, String password, String role, String createdAt) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Admin(int id, String username, String password, String role, String createdAt) {
        this(username, password, role, createdAt);
        this.id = id;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
