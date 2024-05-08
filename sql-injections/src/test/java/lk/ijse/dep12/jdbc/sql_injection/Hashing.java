package lk.ijse.dep12.jdbc.sql_injection;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
public class Hashing {

    @ValueSource(strings = {"admin123", "hip123", "pradeep123"})
    @ParameterizedTest
    void generateHash(String plainPassword) {
        String hash = DigestUtils.sha256Hex(plainPassword);
        System.out.println(plainPassword + ": " + hash);
    }

    @CsvSource(textBlock = """
           admin123,    240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9,
           hip123,      d567f34d84f1347637c17253699012f9314dc9fbf6f6d2d39b20cd1f51a5c1d8,
           pradeep123,  01127f245074c4004d5075ee6899f1ea62e0e332f98b3b97d21dccb890e19035
    """)
    @ParameterizedTest
    void verifyHash(String plainPassword, String expectedHash) {
        String actualHash = DigestUtils.sha256Hex(plainPassword);
        assertEquals(expectedHash, actualHash);
    }
}
