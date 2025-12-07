package com.hotel.dao;

import com.hotel.model.Room;
import java.sql.SQLException;
import java.util.List;

public interface RoomDAO {
    int addRoom(Room room) throws SQLException;
    Room getRoomById(int roomId) throws SQLException;
    List<Room> getAllRooms() throws SQLException;
    List<Room> getAvailableRooms() throws SQLException;
    List<Room> getRoomsByType(String roomType) throws SQLException;
    void updateRoom(Room room) throws SQLException;
    void updateRoomStatus(int roomId, String status) throws SQLException;
    void deleteRoom(int roomId) throws SQLException;
}
