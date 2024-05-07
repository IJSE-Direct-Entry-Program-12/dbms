package lk.ijse.dep12.jdbc.first_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.first_project.db.SingletonConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Class.forName("lk.ijse.dep12.jdbc.first_project.db.SingletonConnection");
        Connection connection = SingletonConnection.getInstance().getConnection();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("JRE is about to exit");
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));

        primaryStage.setScene(new Scene(FXMLLoader.
                load(getClass().getResource("/view/MainView.fxml"))));
        primaryStage.setTitle("JDBC First Project");
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
