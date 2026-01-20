package com.hotel.util;
import com.hotel.model.Room;

public interface RoomRepository {
    Room getRoomById(int id);
    void updateRoom(Room room);
}