package lk.ijse.dep12.jdbc.first_project.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.dep12.jdbc.first_project.to.Student;

import java.sql.*;

public class MainViewController {

    public Button btnDelete;
    public Button btnNewStudent;
    public Button btnSave;
    public TableView<Student> tblStudent;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtId;
    public TextField txtName;

    public void initialize(){
        btnDelete.setDisable(true);

        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudent.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblStudent.getSelectionModel().selectedItemProperty().addListener((ov, previous, current) -> {
            btnDelete.setDisable(current == null);
            btnSave.setDisable(current != null);
        });
        tblStudent.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Student>) change -> {
            if (tblStudent.getSelectionModel().getSelectedItems().size() > 1){
                for (TextField txt : new TextField[]{txtId, txtName, txtAddress, txtContact}) {
                    txt.setText("MULTIPLE SELECTION - (%d) SELECTED".formatted(
                            tblStudent.getSelectionModel().getSelectedItems().size()
                    ));
                }
            }else if (tblStudent.getSelectionModel().getSelectedItems().size() == 1){
                Student selectedStudent = tblStudent.getSelectionModel().getSelectedItem();
                txtId.setText(selectedStudent.getId());
                txtName.setText(selectedStudent.getName());
                txtAddress.setText(selectedStudent.getAddress());
                txtContact.setText(selectedStudent.getContact());
            }else{
                for (TextField txt : new TextField[]{txtId, txtName, txtAddress, txtContact}) txt.clear();
                txtId.setText("GENERATED ID");
            }
        });

        loadAllStudents();
    }

    private void loadAllStudents(){
        try (Connection connection = DriverManager.
                getConnection("jdbc:postgresql://localhost:12500/dep12_first_project",
                "postgres", "psql")) {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("TABLE student");
            ObservableList<Student> studentList = tblStudent.getItems();
            while (rst.next()) {
                String id = formatStudentId(rst.getInt("id"));
                String name = rst.getString("name");
                String address = rst.getString("address");
                String contact = rst.getString("contact");
                Student student = new Student(id , name, address, contact);
                studentList.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load student details, try again").show();
        }
    }

    private String formatStudentId(int id){
        return "S-%03d".formatted(id);
    }

    public void btnDeleteOnAction(ActionEvent event) {

    }

    public void btnNewStudentOnAction(ActionEvent event) {

    }

    public void btnSaveOnAction(ActionEvent event) {

    }

    public void tblStudentOnKeyPressed(KeyEvent event) {

    }

}
