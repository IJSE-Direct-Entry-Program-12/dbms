package lk.ijse.dep12.jdbc.sql_injection.db;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingletonConnection {
    private final static SingletonConnection INSTANCE = new SingletonConnection();
    private Connection CONNECTION;

    private SingletonConnection(){
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/application.properties"));
            String url = properties.getProperty("app.db.url");
            String username = properties.getProperty("app.db.username");
            String password = properties.getProperty("app.db.password");
            CONNECTION = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to establish the database connection").show();
            System.exit(1);
        }
    }

    public static SingletonConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection(){
        return CONNECTION;
    }
}
