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
 * Adapter handling calls to kitafinder.
 * 
 * @author m.zollbrecht
 */
@Slf4j
@Component
public class KitaFinderApiAdapter {

    @Value("${app.kitafinderAdapter.baseUrl}")
    private String baseUrl;

    @Value("${app.kitafinderAdapter.exportRelativeUrl}")
    private String relativeUrl;

    @Autowired
    private RestTemplate restTemplate;

    public KitaFinderApiAdapter() {
        super();
    }

    public ResponseEntity<KitafinderExport> exportKitaData(@NotNull String traeger,
            String kitaIdExtern, @NotNull String password) {
        log.info("Requesting export from kita-planer for traeger={} and kitaIdExtern={}", traeger, kitaIdExtern);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Spring can by default only handle MultiValueMap with MediaType.APPLICATION_FORM_URLENCODED
        MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.put("csv", List.of("n"));
        requestParameters.put("benutzerId", List.of("kita-app-eai"));
        requestParameters.put("traeger", List.of(traeger));
        requestParameters.put("passwort", List.of(password));
        if (kitaIdExtern != null) {
            requestParameters.put("kitaIdExtern", List.of(kitaIdExtern));
        }

        HttpEntity<?> httpRequest = new HttpEntity<>(requestParameters, headers);

        log.debug("Starting export from kita-planer...");
        ResponseEntity<KitafinderExport> response = restTemplate.exchange(baseUrl + relativeUrl,
                HttpMethod.POST,
                httpRequest,
                KitafinderExport.class);
        log.debug("Export from kita-planer done.");
        return response;
    }

}
