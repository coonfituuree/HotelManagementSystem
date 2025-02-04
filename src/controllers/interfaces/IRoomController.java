package controllers.interfaces;

public interface IRoomController {
    String createRoom(models.Room room);
    String getRoomById(int id);
    String getAllRooms();
    String updateRoomStatus(int id, String newStatus);
}
