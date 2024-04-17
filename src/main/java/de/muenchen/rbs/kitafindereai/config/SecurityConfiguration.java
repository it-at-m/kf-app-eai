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
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
    @Order(1)
    @Profile("!no-security")
    public SecurityFilterChain internalApiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/internal/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

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

    @Bean
    @Order(3)
    @Profile("!no-security")
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/actuator/info", "/actuator/health/**", "/explorer/**", "/h2-console/**",
                        "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(Customizer.withDefaults()));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Profile("!no-security")
    public UserDetailsService userDetailsService(@Value("${app.internalApi.authentication.user}") String user,
            @Value("${app.internalApi.authentication.password}") String password) {
        UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails userDetails = users.username(user).password(password).build();

        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    @Profile("no-security")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http)
            throws Exception {
        log.warn("Using mode 'no-security'!");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Configuration
    @SecurityScheme(name = "OAUTH2", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${app.security.token-url}")))
    @SecurityScheme(name = "BasicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    public class SpringdocConfig {
    }

}