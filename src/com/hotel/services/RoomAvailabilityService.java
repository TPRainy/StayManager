package com.hotel.services;

import com.hotel.model.Room;
import com.hotel.repositories.RoomRepository;
import com.hotel.util.SearchResult;

import java.time.LocalDate;
import java.util.List;

public class RoomAvailabilityService {

    private final RoomRepository roomRepo;

    public RoomAvailabilityService(RoomRepository roomRepo){
        this.roomRepo=roomRepo;
    }

    public boolean isRoomAvailable(int roomId) {
        Room room = roomRepo.getById(roomId);
        if (room == null) throw new RuntimeException("Room not found");
        return room.isAvailable();
    }
    public List<Room> getAvailableRooms(LocalDate checkIn, LocalDate checkOut){
        if (checkIn.isBefore(LocalDate.now())){
            throw new RuntimeException("Choose proper date(not past)");
        }
        if (checkOut.isBefore(checkIn)){
            throw new RuntimeException("Date of check out can't be before check in");
        }
        SearchResult<Room> result=roomRepo.findAvailableByDates(checkIn,checkOut);
        return result.getData();
    }
}
