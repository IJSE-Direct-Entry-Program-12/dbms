package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    public Button btnExit;
    public Button btnLogin;
    public PasswordField txtPassword;
    public TextField txtUsername;

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        // Login Logics

        System.setProperty("app.principal.fullName", "Kasun Sampath");
        Stage stage = new Stage();
        stage.setOnCloseRequest(Event::consume);
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
        stage.setTitle("SQL Injection: Main");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        ((Stage)(btnLogin.getScene().getWindow())).close();
    }

}
