package com.hotel.repositories;

import com.hotel.model.Guest;
import com.hotel.model.Reservation;
import com.hotel.model.Room;
import com.hotel.util.DBConnector;
import com.hotel.util.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresReservationRepository implements ReservationRepository {
    private final IDB db = new DBConnector();
    private final GuestRepository guestRepo= new PostgresGuestRepository();
    private final RoomRepository roomRepo=new PostgresRoomRepository();

    @Override
    public Reservation save(Reservation r) {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in, check_out, total_price, is_paid,options) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, r.getGuest().getId());
            stmt.setInt(2, r.getRoom().getId());
            stmt.setDate(3, Date.valueOf(r.getCheckIn()));
            stmt.setDate(4, Date.valueOf(r.getCheckOut()));
            stmt.setDouble(5, r.getTotal());
            stmt.setBoolean(6, r.isPaid());
            stmt.setString(7,r.getOptions());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) r.setId(rs.getInt(1));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int guestId = rs.getInt("guest_id");
                int roomId= rs.getInt("room_id");

                Guest realGuest = guestRepo.getById(guestId);
                Room realRoom = roomRepo.getById(roomId);

                Reservation r = new Reservation(
                        rs.getInt("id"),
                        realGuest,
                        realRoom,
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getString("options")
                );
                r.setPaid(rs.getBoolean("is_paid"));
                return r;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void update(Reservation r) {
        String sql = "UPDATE reservations SET is_paid = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, r.isPaid());
            stmt.setInt(2, r.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override public void delete(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int guestId = rs.getInt("guest_id");
                int roomId = rs.getInt("room_id");

                Guest realGuest = guestRepo.getById(guestId);
                Room realRoom = roomRepo.getById(roomId);

                Reservation r = new Reservation(
                        rs.getInt("id"),
                        realGuest,
                        realRoom,
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getDouble("total_price"),
                        rs.getString("options")
                );
                r.setPaid(rs.getBoolean("is_paid"));
                reservations.add(r);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return reservations;
    }
}
