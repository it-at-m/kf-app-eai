/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
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
    @Profile("!no-security")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/info", "/actuator/health/**", "/explorer/**", "/h2-console/**",
                                "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        // TODO: BasicAuth for internal endpoint. Keep OAuth for kitaApp/v1.
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    @Profile("!no-security")
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String ssoUri,
            @Value("${springdoc.swagger-ui.oauth.clientId}") String clientId) {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(ssoUri);
        decoder.setJwtValidator(
                new JwtClaimValidator<>("clientId", cId -> {
                    return cId != null && clientId.equals(cId);
                }));

        return decoder;
    }

    @Bean
    @Profile("no-security")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http)
            throws Exception {
        log.warn("Using mode 'no-security'!");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }

    @Configuration
    @SecurityScheme(name = "OAUTH2", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${app.token-url}", scopes = {
            @OAuthScope(name = "openid") })))
    @SecurityScheme(name = "BasicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    public class SpringdocConfig {
    }

}