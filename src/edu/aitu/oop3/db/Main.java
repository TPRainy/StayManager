package edu.aitu.oop3.db; // Исправленный путь

import edu.aitu.oop3.db.data.IDB;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Создаем базу через интерфейс - требование Milestone 1
        IDB db = new DatabaseConnection();

        try (Connection connection = db.getConnection()) {
            if (connection != null) {
                System.out.println("Connected successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}