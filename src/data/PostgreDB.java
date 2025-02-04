package data;

import data.interfaces.IDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreDB implements IDB {
    private final String connectionUrl;
    private final String username;
    private final String password;
    private Connection connection;

    public PostgreDB(String host, String username, String password, String dbName) {
        this.username = username;
        this.password = password;
        this.connectionUrl = "jdbc:postgresql://" + host + "/" + dbName;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(connectionUrl, username, password);
                System.out.println("Connected to database.");
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close database connection: " + e.getMessage());
        }
    }
}
