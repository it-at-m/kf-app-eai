/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
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
    public SecurityFilterChain internalApiSecurityFilterChain(HttpSecurity http,
            @Qualifier("internalTokenIntrospector") OpaqueTokenIntrospector introspector) throws Exception {
        http.securityMatcher("/internal/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(config -> config.introspector(introspector)));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Primary
    @Bean("internalTokenIntrospector")
    public OpaqueTokenIntrospector internalTokenIntrospector(
            @Value("${app.security.introspection-url}") String introspectionUri,
            @Value("${app.security.internal.client-id}") String clientId,
            @Value("${app.security.internal.client-secret}") String clientSecret) {
        return new SpringOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }

    /** Security for {@link KitaAppApiController} */
    @Bean
    @Order(2)
    @Profile("!no-security")
    public SecurityFilterChain kitaAppApiSecurityFilterChain(HttpSecurity http,
            @Qualifier("apiTokenIntrospector") OpaqueTokenIntrospector introspector) throws Exception {
        http.securityMatcher("/kitaApp/**")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .opaqueToken(config -> config.introspector(introspector)));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean("apiTokenIntrospector")
    public OpaqueTokenIntrospector apiTokenIntrospector(
            @Value("${app.security.introspection-url}") String introspectionUri,
            @Value("${app.security.api.client-id}") String clientId,
            @Value("${app.security.api.client-secret}") String clientSecret) {
        return new SpringOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
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
    @SecurityScheme(name = "ApiClient", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${app.security.token-url}")))
    @SecurityScheme(name = "InternalLogin", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "${app.security.token-url}")))
    public class SpringdocConfig {
    }

}