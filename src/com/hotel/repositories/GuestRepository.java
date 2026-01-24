package com.hotel.repositories;
import com.hotel.model.Guest;

public interface GuestRepository {
    Guest getGuestById(int id);
    Guest saveGuest(Guest guest);
}