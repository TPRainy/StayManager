package com.hotel.RoomManagementComponent.Repositories;
import com.hotel.RoomManagementComponent.Models.Room;
import com.hotel.Shared.CrudRepository;
import com.hotel.ReservationComponent.Utils.SearchResult;

import java.time.LocalDate;

public interface RoomRepository extends CrudRepository<Room> {
    SearchResult<Room> findAvailableByDates(LocalDate checkIn, LocalDate checkOut);
}