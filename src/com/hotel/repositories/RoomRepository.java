package com.hotel.repositories;
import com.hotel.model.Room;
import com.hotel.util.SearchResult;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends CrudRepository<Room> {
    SearchResult<Room> findAvailableByDates(LocalDate checkIn, LocalDate checkOut);
}