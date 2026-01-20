package com.hotel.util;
import com.hotel.model.Guest;

public interface GuestRepository {
    Guest getGuestById(int id);
}