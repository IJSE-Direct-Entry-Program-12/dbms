package lk.ijse.dep12.jdbc;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FeedEmployees {

    public static void main(String[] args) {
        try (Connection connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")) {
            Faker faker = new Faker();
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                Statement stm = connection.createStatement();

                String firstName = faker.name().firstName().replace("'", "");
                String lastName = faker.name().lastName().replace("'", "");
                String address = faker.address().streetAddress().replace("'", "");
                String country = faker.country().name().replace("'", "");
                String contact = faker.regexify("0[1-9]{2}-\\d{7}");
                String nic = faker.regexify("\\d{9}V");

                String sql = """
                        INSERT INTO employee (first_name, last_name, address, country, contact, nic) 
                            VALUES ('%s', '%s', '%s', '%s', '%s', '%s')
                            """.formatted(firstName, lastName, address, country, contact, nic);

                int affectedRows = stm.executeUpdate(sql);
//                if (affectedRows == 1){
//                    System.out.println(i + "- employee added");
//                }else{
//                    System.out.println(i + "- employee failed to add");
//                }
            }
            long t2 = System.currentTimeMillis();
            System.out.println(t2 - t1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
