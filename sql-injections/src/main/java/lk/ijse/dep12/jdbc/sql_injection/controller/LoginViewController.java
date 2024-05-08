package lk.ijse.dep12.jdbc.sql_injection.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dep12.jdbc.sql_injection.db.SingletonConnection;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginViewController {

    public Button btnExit;
    public Button btnLogin;
    public PasswordField txtPassword;
    public TextField txtUsername;

    public void btnExitOnAction(ActionEvent event) {
        Platform.exit();
    }

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.isBlank() || username.strip().contains(" ") || username.strip().length() < 3 ||
            password.isBlank() || password.strip().length() < 4 || !password.matches(".*\\d.*")
            || !password.matches(".*[A-Za-z].*")) {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password, try again").show();
            txtUsername.requestFocus();
            txtUsername.selectAll();
            return;
        }

        try {
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("""
                        SELECT FROM "user" WHERE username = ?
                    """);
            stm.setString(1, username.strip());
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {       // Let's check whether user exists or not?
                stm = connection.prepareStatement("""
                                SELECT * FROM "user" WHERE username = ?
                        """);
                stm.setString(1, username.strip());
                rst = stm.executeQuery();
                rst.next();
                String hashPassword = rst.getString("password");
                /* Let's verify the password */
                if (hashPassword.equals(DigestUtils.sha256Hex(password))){
                    String fullName = rst.getString("full_name");
                    System.setProperty("app.principal.fullName", fullName);
                    Stage stage = new Stage();
                    stage.setOnCloseRequest(Event::consume);
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainView.fxml"))));
                    stage.setTitle("SQL Injection: Main");
                    stage.setResizable(false);
                    stage.show();
                    stage.centerOnScreen();
                    ((Stage) (btnLogin.getScene().getWindow())).close();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Invalid username or password, try again").show();
                    txtUsername.requestFocus();
                    txtUsername.selectAll();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid username or password, try again").show();
                txtUsername.requestFocus();
                txtUsername.selectAll();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
            return;
        }


    }

}
