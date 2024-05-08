package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainViewController {

    public Button btnLogOut;
    public Label lblDateTime;
    public Label lblFullName;

    public void initialize(){
        Task<String> dateTimeTask = new Task<>(){

            @Override
            protected String call() throws Exception {
                while (true) {
                    updateValue(LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                    Thread.sleep(1000);
                }
            }
        };

        lblDateTime.textProperty().bind(dateTimeTask.valueProperty());
        new Thread(dateTimeTask).start();

        String fullName = System.getProperty("app.principal.fullName");
        lblFullName.setText(fullName);
    }

    public void btnLogOutOnAction(ActionEvent event) throws IOException {
        System.getProperties().remove("app.principal.fullName");
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"))));
        stage.setTitle("SQL Injection: Login");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage)(btnLogOut.getScene().getWindow())).close();
    }


}
