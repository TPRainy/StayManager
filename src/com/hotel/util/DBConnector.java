package com.hotel.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector implements IDB {

    // Поля для подключения
    private static final String URL = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
    private static final String USER = "postgres.imumyrbfjafmmvkrycts";
    private String password;

    // Singleton
    private static DBConnector instance;
    private Connection connection;

    // конструктор
    private DBConnector() {
        try {
            this.password = loadPassword();
            System.out.println("--- DB DEBUG (Singleton) ---");
            System.out.println("Password loaded? " + (password != null && !password.isEmpty()));

            this.connection = DriverManager.getConnection(URL, USER, password);
            System.out.println("✅ Connected to Database successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static synchronized DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        try {
            // Если соединение закрылось, переоткрываем его
            if (instance.connection == null || instance.connection.isClosed()) {
                instance = new DBConnector();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    // Метод из интерфейса IDB
    @Override
    public Connection getConnection() {
        return instance.connection;
    }

    private String loadPassword() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
            String value = props.getProperty("DB_PASSWORD");
            if (value == null) return "";
            return value.trim();
        } catch (IOException e) {
            System.err.println("⚠️ config.properties not found! Check project root.");
            return "";
        }
    }
}
