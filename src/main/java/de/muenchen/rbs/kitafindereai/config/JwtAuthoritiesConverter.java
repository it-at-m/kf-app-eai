/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * 
 */
public class JwtAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // lade Rollen aus folgender Struktur:
        // "resource_access": {
        //   "kf-app-eai": {
        //     "roles": [
        //       "lhm-ab-kf-app-eai-internal-access",
        //       "internal-access"
        //     ]
        //   }
        // }
        final Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("resource_access",
                Map.of());
        final Map<String, Object> kfAppEai = (Map<String, Object>) resourceAccess.getOrDefault("kf-app-eai", Map.of());
        final Collection<String> roles = (Collection<String>) kfAppEai.getOrDefault("roles", List.of());

        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toCollection(HashSet::new));
    }
}
