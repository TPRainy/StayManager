package com.hotel.repositories;

import com.hotel.model.Guest;
import com.hotel.util.DBConnector;
import com.hotel.util.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresGuestRepository implements GuestRepository{
    private final IDB db=new DBConnector();

    @Override
    public Guest getById(int id) {
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
    public Guest save(Guest guest){
        String sql="INSERT INTO guests (first_name, last_name, email, phone) VALUES (?,?,?,?)";
        try (Connection conn= db.getConnection();
             PreparedStatement stmt= conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2,guest.getLastName());
            stmt.setString(3,guest.getEmail());
            stmt.setString(4,guest.getPhone());

            int affectedRows=stmt.executeUpdate();

            if (affectedRows>0){
                try (ResultSet rs = stmt.getGeneratedKeys()){
                    if(rs.next()){
                        int newId=rs.getInt(1);
                        return new Guest(newId,guest.getFirstName(),guest.getLastName(),guest.getEmail(),guest.getPhone());
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Guest> getAll() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                guests.add(new Guest(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return guests;
    }

    @Override
    public void update(Guest guest) {

        String sql = "UPDATE guests SET first_name=?, last_name=?, email=?, phone=? WHERE id=?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getEmail());
            stmt.setString(4, guest.getPhone());
            stmt.setInt(5, guest.getId());

            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM guests WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
