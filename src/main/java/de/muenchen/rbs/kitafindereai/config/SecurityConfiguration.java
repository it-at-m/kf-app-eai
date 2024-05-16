/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
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

    private static final String AUD_CLAIM = "aud";

    /** Security for {@link InternalApiController} */
    @Bean
    @Order(1)
    @Profile("!no-security")
    public SecurityFilterChain internalApiSecurityFilterChain(HttpSecurity http,
            JwtDecoder decoder, JwtAuthenticationConverter authConverter) throws Exception {
        http.securityMatcher("/internal/**")
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(decoder)
                                .jwtAuthenticationConverter(authConverter)));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    /** Security for {@link KitaAppApiController} */
    @Bean
    @Order(2)
    @Profile("!no-security")
    public SecurityFilterChain kitaAppApiSecurityFilterChain(HttpSecurity http,
            JwtDecoder decoder, JwtAuthenticationConverter authConverter) throws Exception {
        http.securityMatcher("/kitaApp/**")
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(decoder)
                                .jwtAuthenticationConverter(authConverter)));
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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http,
            JwtDecoder decoder, JwtAuthenticationConverter authConverter) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/actuator/info", "/actuator/health/**",
                        "/swagger-ui/**", "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated())
                .oauth2ResourceServer((oauth2) -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(decoder)
                                .jwtAuthenticationConverter(authConverter)));
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    @Profile("!no-security")
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    @Profile("!no-security")
    JwtDecoder jwtDecoder(@Value("${app.security.issuer-url}") String issuerUri,
            @Value("${app.security.client-id}") String requiredAudience) {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = audienceValidator(requiredAudience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    OAuth2TokenValidator<Jwt> audienceValidator(String requiredAudience) {
        return new JwtClaimValidator<List<String>>(AUD_CLAIM, aud -> aud.contains(requiredAudience));
    }

    /** Security-config for profile 'no-security' */
    @Bean
    @Profile("no-security")
    public SecurityFilterChain noSecurityFilterChain(HttpSecurity http)
            throws Exception {
        log.warn("Using mode 'no-security'!");
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        http.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable()).headers(headers -> headers
                .frameOptions(FrameOptionsConfig::disable));
        return http.build();
    }

    /** Swagger-API config for security */
    @Configuration
    @Profile("!no-security")
    @SecurityScheme(name = "ApiClient", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(clientCredentials = @OAuthFlow(tokenUrl = "${app.security.token-url}")))
    @SecurityScheme(name = "InternalLogin", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(authorizationCode = @OAuthFlow(tokenUrl = "${app.security.token-url}", authorizationUrl = "${app.security.authorization-url}", refreshUrl = "${app.security.token-url}")))
    public class SpringdocConfig {
    }

}