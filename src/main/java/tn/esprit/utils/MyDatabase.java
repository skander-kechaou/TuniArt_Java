package tn.esprit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private final String URL="jdbc:mysql://localhost:3306/tuni'art"; //API:DBMS:ADDRESS:PORT
    private final String USERNAME="root";
    private final String PWD="";
    private Connection conn;
    public static MyDatabase instance;

    private MyDatabase() {
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PWD);
            System.out.println("Connected.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static MyDatabase getInstance(){
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getConn(){
        return conn;
    }
}
