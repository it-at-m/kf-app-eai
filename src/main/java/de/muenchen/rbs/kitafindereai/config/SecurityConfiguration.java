/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.muenchen.rbs.kitafindereai.api.InternalApiController;
import de.muenchen.rbs.kitafindereai.api.KitaAppApiController;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

/**
 * The central class for configuration of all security aspects.
 * 
 * @author m.zollbrecht
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    /** Security for {@link InternalApiController} */
    @Bean
    @Order(1)
    @Profile("!no-security")
    public SecurityFilterChain internalApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/internal/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    /** Security for {@link KitaAppApiController} */
    @Bean
    @Order(2)
    @Profile("!no-security")
    public SecurityFilterChain kitaAppApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/kitaApp/**")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(Customizer.withDefaults()));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    /**
     * Security for remaining endpoints.
     * Excluding Swagger UI and spring actuators.
     */
    @Bean
    @Order(3)
    @Profile("!no-security")
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/actuator/info", "/actuator/health/**",
                        "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(Customizer.withDefaults()));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    /**
     * UserDetailsService for BasicAuth
     * 
     * @param user User for BasicAuth
     * @param password Password for BasicAuth
     * @return UserDetailsService for BasicAuth
     */
    @Bean
    @Profile("!no-security")
    public UserDetailsService userDetailsService(@Value("${app.internalApi.authentication.user}") String user,
            @Value("${app.internalApi.authentication.password}") String password) {
        UserDetails userDetails = User.withDefaultPasswordEncoder().username(user).password(password).build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    /** Security-config for profile 'no-security' */
    @Bean
    @Profile("no-security")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http)
            throws Exception {
        log.warn("Using mode 'no-security'!");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    /** Swagger-API config for security */
    @Configuration
    @SecurityScheme(name = "OAUTH2", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${app.security.token-url}")))
    @SecurityScheme(name = "BasicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    public class SpringdocConfig {
    }

}