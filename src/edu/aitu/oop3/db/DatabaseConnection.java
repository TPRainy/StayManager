package edu.aitu.oop3.db;

import edu.aitu.oop3.db.data.IDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements IDB { // Добавили implements

    @Override
    public Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://aws-1-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
        String user = "postgres.imumyrbfjafmmvkrycts";
        String password = "твой_пароль";

        return DriverManager.getConnection(url, user, password);
    }
}