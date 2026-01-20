package com.hotel.util;

import com.hotel.model.*;
import java.sql.*;

public class PostgresRepository implements GuestRepository, RoomRepository, ReservationRepository, PaymentRepository {

    private final IDB db = new DBConnector();

    @Override
    public Guest getGuestById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new Guest(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("phone"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Room getRoomById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new Room(rs.getInt("id"), rs.getString("room_number"), rs.getString("type"), rs.getDouble("price"), rs.getBoolean("is_available"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void updateRoom(Room room) {
        String sql = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, room.isAvailable());
            stmt.setInt(2, room.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void saveReservation(Reservation r) {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in, check_out, total_price, is_paid) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, r.getGuest().getId());
            stmt.setInt(2, r.getRoom().getId());
            stmt.setDate(3, Date.valueOf(r.getCheckIn()));
            stmt.setDate(4, Date.valueOf(r.getCheckOut()));
            stmt.setDouble(5, r.getTotal());
            stmt.setBoolean(6, r.isPaid());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) r.setId(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public Reservation getReservationById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int guestId = rs.getInt("guest_id");
                Guest dummyGuest = new Guest(guestId, "Guest #" + guestId, "Unknown", "Unknown", "Unknown");

                Room dummyRoom = new Room(rs.getInt("room_id"), "Unknown", "Unknown", 0, false);

                Reservation r = new Reservation(
                        rs.getInt("id"),
                        dummyGuest,
                        dummyRoom,
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getDouble("total_price")
                );
                r.setPaid(rs.getBoolean("is_paid"));
                return r;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void updateReservation(Reservation r) {
        String sql = "UPDATE reservations SET is_paid = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, r.isPaid());
            stmt.setInt(2, r.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public void deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void savePayment(Payment p) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getReservationId());
            stmt.setDouble(2, p.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(p.getPaymentDate()));
            stmt.setString(4, p.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}