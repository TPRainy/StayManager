package com.hotel.RoomManagementComponent.Repositories;

import com.hotel.RoomManagementComponent.Models.Room;
import com.hotel.Shared.DBConnector;
import com.hotel.Shared.IDB;
import com.hotel.RoomManagementComponent.Utils.RoomFactory;
import com.hotel.ReservationComponent.Utils.SearchResult;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostgresRoomRepository implements RoomRepository {
    private final IDB db= DBConnector.getInstance();
    @Override
    public Room getById(int id) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return RoomFactory.createRoom(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Room> getAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(RoomFactory.createRoom(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rooms;
    }

    @Override
    public Room save(Room room) {
        String sql = "INSERT INTO rooms (room_number, type, price, is_available) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setString(2, room.getType()); // "VIP" или "Standard"
            stmt.setDouble(3, room.getPricePerNight());
            stmt.setBoolean(4, room.isAvailable());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        return RoomFactory.createRoom(newId, room.getRoomNumber(), room.getType(), room.getPricePerNight(), room.isAvailable());
                    }
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void update(Room room) {
        String sql = "UPDATE rooms SET is_available = ?, price = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setBoolean(1, room.isAvailable());
            stmt.setDouble(2, room.getPricePerNight());
            stmt.setString(3, room.getType());
            stmt.setInt(4, room.getId());
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public SearchResult<Room> findAvailableByDates(LocalDate start, LocalDate end) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE id NOT IN (" +
                "SELECT room_id FROM reservations " +
                "WHERE NOT (check_out <= ? OR check_in >= ?))";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(start));
            stmt.setDate(2, java.sql.Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Room room = RoomFactory.createRoom(rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                );
                rooms.add(room);
            }
            return new SearchResult<>(rooms,"Found available rooms: "+rooms.size(),true);
        } catch (SQLException e) {
            return new SearchResult<>(new ArrayList<>(), "Error searching: "+e.getMessage(),false);}
    }
}
