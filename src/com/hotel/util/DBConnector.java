package com.hotel.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector implements IDB {
    @Override
    public Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }
}