package lk.ijse.dep12.jdbc.sql_injection.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class SingletonConnectionTest {

    @Test
    void getInstance() {
        SingletonConnection instance1 = SingletonConnection.getInstance();
        SingletonConnection instance2 = SingletonConnection.getInstance();
        SingletonConnection instance3 = SingletonConnection.getInstance();

        assertEquals(instance1, instance2);
        assertEquals(instance1, instance3);
    }

    @Test
    void getConnection() {
        Connection connection1 = SingletonConnection.getInstance().getConnection();
        Connection connection2 = SingletonConnection.getInstance().getConnection();
        Connection connection3 = SingletonConnection.getInstance().getConnection();

        System.out.println(connection1);
        System.out.println(connection2);
        System.out.println(connection3);

        assertEquals(connection1, connection2);
        assertEquals(connection1, connection3);
    }
}