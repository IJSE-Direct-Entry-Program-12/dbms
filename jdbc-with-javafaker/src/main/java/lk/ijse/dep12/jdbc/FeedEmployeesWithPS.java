package lk.ijse.dep12.jdbc;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedEmployeesWithPS {

    public static void main(String[] args) {
        try (Connection connection = DriverManager.
                getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")) {
            String sql = """
                    INSERT INTO employee (first_name, last_name, address, country, contact, nic) 
                    VALUES (?, ?, ?, ?, ?, ?)
                    """;
            Faker faker = new Faker();

            long t1 = System.currentTimeMillis();
            PreparedStatement stm = connection.prepareStatement(sql);
            for (int i = 0; i < 1000; i++) {
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                String address = faker.address().streetAddress();
                String country = faker.country().name();
                String contact = faker.regexify("0[1-9]{2}-\\d{7}");
                String nic = faker.regexify("\\d{9}V");

                stm.setString(1, firstName);
                stm.setString(2, lastName);
                stm.setString(3, address);
                stm.setString(4, country);
                stm.setString(5, contact);
                stm.setString(6, nic);

                int affectedRow = stm.executeUpdate();
//                if (affectedRow == 1) System.out.println(i + "- employee added");
//                else System.out.println(i + "- employee failed to add");
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
