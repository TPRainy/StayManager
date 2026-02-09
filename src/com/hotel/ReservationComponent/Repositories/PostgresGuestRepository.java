package com.hotel.ReservationComponent.Repositories;

import com.hotel.ReservationComponent.Models.Guest;
import com.hotel.Shared.DBConnector;
import com.hotel.Shared.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresGuestRepository implements GuestRepository {
    private final IDB db=DBConnector.getInstance();
    
    @Override
    public Guest getById(int id) {
        String sql = "SELECT * FROM guests WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new Guest(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getString("phone"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Guest save(Guest guest){
        String sql="INSERT INTO guests (first_name, last_name, email, phone) VALUES (?,?,?,?)";
        try (PreparedStatement stmt= db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
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

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql);
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

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {

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

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
