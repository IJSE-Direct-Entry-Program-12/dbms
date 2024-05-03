package lk.ijse.dep12.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {

    public static void main(String[] args) {
        try (Connection connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")) {
            Statement stm = connection.createStatement();
            String sql = """
                    INSERT INTO student (name, address, contact) 
                    VALUES  ('Kasun', 'Galle', '011-1234567'),
                            ('Nuwan', 'Matara', '012-1234567'),
                            ('Upul', 'Moratuwa', '033-1234567')
                    """;
            int affectedRows = stm.executeUpdate(sql);
            if (affectedRows == 3){
                System.out.println("Records inserted");
            }else{
                System.out.println("Something went wrong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
