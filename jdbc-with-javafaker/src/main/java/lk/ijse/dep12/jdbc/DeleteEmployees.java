package lk.ijse.dep12.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteEmployees {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.
                getConnection("jdbc:postgresql://localhost:12500/dep12",
                "postgres", "psql")) {
            Statement stm = connection.createStatement();
            // int affectedRows = stm.executeUpdate("TRUNCATE employee");
            int affectedRows = stm.executeUpdate("DELETE FROM employee WHERE TRUE");
            if (affectedRows > 0) System.out.println("All employee records deleted");
            else System.out.println("Something went wrong");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
