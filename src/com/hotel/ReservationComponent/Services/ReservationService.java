package com.hotel.ReservationComponent.Services;

import com.hotel.ReservationComponent.Exceptions.InvalidDateException;
import com.hotel.ReservationComponent.Exceptions.RoomNotAvailableException;
import com.hotel.ReservationComponent.Models.Guest;
import com.hotel.ReservationComponent.Models.Reservation;
import com.hotel.ReservationComponent.Models.ReservationDetails;
import com.hotel.RoomManagementComponent.Models.Room;
import com.hotel.ReservationComponent.Repositories.GuestRepository;
import com.hotel.ReservationComponent.Repositories.ReservationRepository;
import com.hotel.RoomManagementComponent.Repositories.RoomRepository;
import com.hotel.ReservationComponent.Utils.SeasonCalendar;
import com.hotel.ReservationComponent.Utils.Notification;
import com.hotel.ReservationComponent.Utils.NotificationFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationService {

    private final GuestRepository guestRepo;
    private final RoomRepository roomRepo;
    private final ReservationRepository reservationRepo;
    private final RoomAvailabilityService availabilityService;

    public ReservationService(GuestRepository guestRepo, RoomRepository roomRepo, ReservationRepository reservationRepo, RoomAvailabilityService availabilityService) {
        this.guestRepo = guestRepo;
        this.roomRepo = roomRepo;
        this.reservationRepo = reservationRepo;
        this.availabilityService = availabilityService;
    }

    public void cancelReservation(int reservationId) {
        Reservation res = reservationRepo.getById(reservationId);
        if (res == null) throw new RuntimeException("Reservation not found");

        Room room = res.getRoom();
        room.setAvailable(true);
        roomRepo.update(room);

        reservationRepo.delete(reservationId);
    }

    public int createReservation(int guestId, int roomId, LocalDate checkIn, LocalDate checkOut, String option) {
        validateDates(checkIn, checkOut);

        Guest guest = guestRepo.getById(guestId);
        Room room = roomRepo.getById(roomId);
        SeasonCalendar calendar = SeasonCalendar.getInstance();

        if (!availabilityService.isRoomAvailable(roomId)) {
            throw new RoomNotAvailableException("Room " + roomId + " is occupied!");
        }


        double basePrice = room.getPricePerNight();
        if (calendar.isHighSeason(checkIn)) {
            basePrice *= 1.5;
        }

        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (days <= 0) days = 1;

        double optionCharge = 0;
        if ("All inclusive".equals(option)) {
            optionCharge = 4000;
        } else if ("Breakfast in room".equals(option)) {
            optionCharge = 3000;
        } else if ("WiFi connection".equals(option)) {
            optionCharge = 1000;
        }

        double pricePerNightWithOption = basePrice + optionCharge;
        double totalPrice = days * pricePerNightWithOption;

        room.setAvailable(false);
        roomRepo.update(room);

        Reservation reservation = new Reservation(0, guest, room, checkIn, checkOut, totalPrice, option);
        reservationRepo.save(reservation);

        String notifType = (option != null && option.contains("WiFi")) ? "EMAIL" : "SMS";

        Notification notification = NotificationFactory.createNotification(notifType);

        notification.send("Dear " + guest.getFirstName() + ", your booking #" + reservation.getId() + " is confirmed! Total: " + totalPrice);

        return reservation.getId();
    }

    public ReservationDetails getFullReservationDetails(int reservationId){
        Reservation res = reservationRepo.getById(reservationId);
        return new ReservationDetails.Builder()
                .setRoom(res.getRoom())
                .setPaymentinfo(res.isPaid() ? "Paid" : "Pending", res.getTotal())
                .addOption(res.getOptions())
                .build();
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) throw new InvalidDateException("Invalid dates!");
        if (checkIn.isBefore(LocalDate.now())) throw new InvalidDateException("Past date!");
    }

    public Guest registerGuest(Guest guest) {
        return guestRepo.save(guest);
    }
}