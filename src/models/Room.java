package models;

public class Room {
    private int id;
    private String roomNumber;
    private String roomType;
    private double price;
    private String status;
    private String createdAt;

    // Конструкторы
    public Room(Integer o, String roomNumber, String roomType, double price, String status, Object createdAt) {}

    public Room(String roomNumber, String roomType, double price, String status, String createdAt) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Room(int id, String roomNumber, String roomType, double price, String status, String createdAt) {
        this(roomNumber, roomType, price, status, createdAt);
        this.id = id;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomType='" + roomType + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
