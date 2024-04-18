/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Component
public class KitaFinderApiAdapter {

    @Value("${app.kitafinderAdapter.baseUrl}")
    private String baseUrl;

    private final String relativeUrl = "/kitaplaner/api/schnittstelle/export.xml";

    @Autowired
    private RestTemplate restTemplate;

    public KitaFinderApiAdapter() {
        super();
    }

    public ResponseEntity<KitafinderExport> exportKitaData(@NotNull String traeger,
            @NotNull String password, String kitaIdExtern) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.put("csv", List.of("n"));
        requestParameters.put("benutzerId", List.of("kita-app-eai"));
        requestParameters.put("traeger", List.of(traeger));
        requestParameters.put("passwort", List.of(password));
        if (kitaIdExtern != null) {
            requestParameters.put("kitaIdExtern", List.of(kitaIdExtern));
        }

        HttpEntity<?> httpRequest = new HttpEntity<>(requestParameters, headers);

        ResponseEntity<KitafinderExport> response = restTemplate.exchange(baseUrl + relativeUrl,
                HttpMethod.POST,
                httpRequest,
                KitafinderExport.class);

        return response;
    }

}
