package edu.aitu.oop3.db.data; // Путь должен быть именно таким!

import java.sql.Connection;
import java.sql.SQLException;

public interface IDB {
    Connection getConnection() throws SQLException;
}