package lk.ijse.dep12.jdbc.rs;

import java.sql.*;

public class Demo2 {

    public static void main(String[] args) {
        try (Connection connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")) {
            Statement stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            // Internal representation of a "ResultSet" object is a doubly-linked-list
            ResultSet rst = stm.executeQuery("TABLE employee");
            rst.absolute(5);
            int id = rst.getInt(1);
            String firstName = rst.getString("first_name");
            String lastName = rst.getString(3);
            String address = rst.getString("address");
            String country = rst.getString("country");
            String contact = rst.getString("contact");
            String nic = rst.getString("nic");
            System.out.printf("id=%s, name=%s, address=%s, country=%s, nic=%s, contact=%s \n",
                    id, firstName + " " + lastName, address, country, nic, contact);
//            rst.updateString("first_name", "Kasun");
//            rst.updateString("last_name", "Sampath");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
