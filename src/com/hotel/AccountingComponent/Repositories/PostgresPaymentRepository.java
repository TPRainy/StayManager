package com.hotel.AccountingComponent.Repositories;

import com.hotel.AccountingComponent.Models.Payment;
import com.hotel.Shared.DBConnector;
import com.hotel.Shared.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresPaymentRepository implements PaymentRepository {
    private final IDB db = DBConnector.getInstance();

    @Override
    public Payment save(Payment p) {
        String sql = "INSERT INTO payments (reservation_id, amount, payment_date, status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, p.getReservationId());
            stmt.setDouble(2, p.getAmount());
            stmt.setTimestamp(3, Timestamp.valueOf(p.getPaymentDate()));
            stmt.setString(4, p.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                        return p;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Payment getById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(), // Конвертируем обратно
                        rs.getString("status")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("reservation_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date").toLocalDateTime(),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return payments;
    }

    @Override
    public void update(Payment p) {
        String sql = "UPDATE payments SET status = ?, amount = ? WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {

            stmt.setString(1, p.getStatus());
            stmt.setDouble(2, p.getAmount());
            stmt.setInt(3, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (PreparedStatement stmt = db.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
