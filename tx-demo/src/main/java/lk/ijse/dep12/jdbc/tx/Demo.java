package lk.ijse.dep12.jdbc.tx;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Demo {

    public static void main(String[] args) {
        try (Connection connection = DriverManager
                .getConnection("jdbc:postgresql://localhost:12500/dep12",
                        "postgres", "psql")) {
            Faker faker = new Faker();
            PreparedStatement stm = connection.prepareStatement("""
                    INSERT INTO employee (first_name, last_name, address, country, contact, nic)
                    VALUES (?, ?, ?, ?, ?, ?)
                    """);

            connection.setAutoCommit(false);                // begin transaction
            try {
                for (int i = 0; i < 100; i++) {
//                    if (i == 25) throw new RuntimeException("Something went wrong");
                    stm.setString(1, faker.name().firstName());
                    stm.setString(2, faker.name().lastName());
                    stm.setString(3, faker.address().city());
                    stm.setString(4, faker.address().country());
                    stm.setString(5, faker.regexify("0[1-9]{2}-\\d{7}"));
                    stm.setString(6, faker.regexify("\\d{9}[V]"));
                    // stm.executeUpdate();
                    stm.addBatch();
                }
                stm.executeBatch();
                // connection.commit();                     // flush
            } catch (Throwable t) {
                connection.rollback();                      // clear
                t.printStackTrace();
            } finally {
                connection.setAutoCommit(true);         // flush + end transaction
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
