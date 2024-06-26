package lk.ijse.dep12.jdbc.first_project.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.dep12.jdbc.first_project.db.SingletonConnection;
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
        try  {
            Statement stm = SingletonConnection.getInstance().getConnection().createStatement();
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
        try  {
            Connection connection = SingletonConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("DELETE FROM student WHERE id = ?");
            ObservableList<Student> selectedStudents = tblStudent.getSelectionModel().getSelectedItems();
            connection.setAutoCommit(false);
            try {
                for (Student selectedStudent : selectedStudents) {
                    stm.setInt(1,
                            Integer.parseInt(selectedStudent.getId().replace("S-", "")));
                    stm.addBatch();
                }
                stm.executeBatch();
                ObservableList<Student> studentList = tblStudent.getItems();
                studentList.removeAll(selectedStudents);
                btnNewStudent.fire();
            }catch (Throwable t){
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Failed to delete students, try again").show();
                t.printStackTrace();
            }finally{
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
        }
    }

    public void tblStudentOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) btnDelete.fire();
    }

    public void btnNewStudentOnAction(ActionEvent event) {
        tblStudent.getSelectionModel().clearSelection();
        for (TextField txt : new TextField[]{txtId, txtName, txtAddress, txtContact}) txt.clear();
        txtId.setText("GENERATED ID");
        txtName.requestFocus();
    }

    public void btnSaveOnAction(ActionEvent event) {
        if (!validateData()) return;

        String name = txtName.getText().strip();
        String address = txtAddress.getText().strip();
        String contact = txtContact.getText().strip();

        try {
            PreparedStatement stm = SingletonConnection.getInstance().getConnection()
                    .prepareStatement("""
                    INSERT INTO student (name, address, contact) VALUES (?, ?, ?)
                    """, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, name);
            stm.setString(2, address);
            stm.setString(3, contact.isEmpty() ? null : contact);
            stm.executeUpdate();
            ResultSet generatedKeys = stm.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt("id");
            ObservableList<Student> studentList = tblStudent.getItems();
            studentList.add(new Student(formatStudentId(id), name, address, contact));
            btnNewStudent.fire();
        } catch (SQLException e) {
            // This is not a good practice at all
            // This should be handled as a business validation
            if (e.getSQLState().equals("23505")){
                new Alert(Alert.AlertType.ERROR, "Contact number already exists").show();
                txtContact.requestFocus();
                txtContact.selectAll();
                return;
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again").show();
        }
    }

    private boolean validateData(){
        String name = txtName.getText().strip();
        if (!name.matches("[A-Za-z ]{3,}")){
            txtName.requestFocus();
            txtName.selectAll();
            return false;
        }
        String address = txtAddress.getText().strip();
        if (address.length() < 3){
            txtAddress.requestFocus();
            txtAddress.selectAll();
            return false;
        }
        String contact = txtContact.getText().strip();
        if (!contact.isEmpty() && !contact.matches("0[1-9]{2}-\\d{7}")){
            txtContact.requestFocus();
            txtContact.selectAll();
            return false;
        }
        return true;
    }

}
