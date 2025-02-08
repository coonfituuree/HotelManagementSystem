package repositories.interfaces;

import models.Room;
import java.util.List;

public interface IRoomRepository {
    boolean createRoom(Room room);
    Room getRoomById(int id);
    List<Room> getAllRooms();
    boolean updateRoomStatus(int id, String newStatus);

    // ДОбавляем метод поиска свободных комнат
    List<Room> findAvailableRooms(String checkIn, String checkOut, double maxPrice);

    String deleteRoomByNumber(String roomNumber);
}
