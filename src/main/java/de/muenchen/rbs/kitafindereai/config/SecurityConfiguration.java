/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

/**
 * The central class for configuration of all security aspects.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    @Profile("!no-security")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/info", "/actuator/health/**", "/explorer/**", "/h2-console/**",
                                "/swagger-ui/**", "/v3/api-docs/**", "/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Profile("no-security")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http)
            throws Exception {
        log.warn("Using mode 'no-security'!");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

}