/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * General configuration for encryption and decryption.
 */
@Configuration
public class TextEncoderConfiguration {

    @Value("${app.passwordEncoder.encryptor.password}")
    private String password;
    
    @Value("${app.passwordEncoder.encryptor.salt}")
    private String salt;
    
    @Bean
    public TextEncryptor encryptor() {
        return Encryptors.delux(password, salt);
    }

}
