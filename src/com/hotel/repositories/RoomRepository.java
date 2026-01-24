package com.hotel.repositories;
import com.hotel.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository {
    Room getRoomById(int id);
    void updateRoom(Room room);
    List<Room> findAvailableByDates(LocalDate checkIn, LocalDate checkOut);
}