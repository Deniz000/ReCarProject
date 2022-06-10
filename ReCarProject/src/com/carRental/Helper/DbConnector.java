package com.carRental.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    Connection connection = null;

    public Connection connectDb(){
        try {
            this.connection = DriverManager.getConnection(Config.DB_URL, Config.DB_USERNAME,Config.DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }
    public static Connection getInstance(){
        DbConnector dbConnector = new DbConnector();
        return dbConnector.connectDb();
    }
}
