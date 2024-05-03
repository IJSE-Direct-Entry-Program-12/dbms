package lk.ijse.dep12.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo1 {

    public static void main(String[] args) {
        try(Connection connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")){
            System.out.println(connection);
            Statement stm = connection.createStatement();
            String sql = "INSERT INTO customer VALUES (1, 'Kasun', 'Galle'), (2, 'Nuwan', 'Matara')";
            int affectedRows = stm.executeUpdate(sql);
            if (affectedRows == 2){
                System.out.println("Kasun and Nuwan have been added");
            }else{
                System.out.println("Something went wrong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
