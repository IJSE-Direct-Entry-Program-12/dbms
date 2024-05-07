package lk.ijse.dep12.jdbc.first_project.db;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {
    private final static SingletonConnection INSTANCE = new SingletonConnection();
    private Connection CONNECTION;

    private SingletonConnection()  {
        try {
            CONNECTION = DriverManager
                    .getConnection("jdbc:postgresql://localhost:12500/dep12_first_project",
                            "postgres", "psql");
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to establish the database connection, try restarting")
                    .showAndWait();
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
