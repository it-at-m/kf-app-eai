package de.muenchen.rbs.kitafindereai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests for {@link TextEncryptor}
 * 
 * @author m.zollbrecht
 */
@SpringBootTest
@ActiveProfiles({ "test", "no-security" })
class PasswordEncoderTest {

    @Autowired
    private TextEncryptor encoder;

    @Test
    void passwordEncryption() {
        int length = 50;
        boolean useLetters = true;
        boolean useNumbers = true;

        for (int i = 0; i < 100; i++) {
            String password = RandomStringUtils.random(length, useLetters, useNumbers);

            String encodedString = encoder.encrypt(password);
            // fits in the corresponding column
            assertThat(encodedString.length()).isLessThanOrEqualTo(255);
            String decryptedPassword = encoder.decrypt(encodedString);
            // properly decodes
            assertEquals(password, decryptedPassword);
        }
    }

}
