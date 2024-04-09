/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.muenchen.rbs.kitafindereai.adapter.model.KitafinderExport;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Component
public class KitaFinderApiAdapter {

    private final String baseUrl;
    private final RestTemplate restTemplate;
    
    public KitaFinderApiAdapter(String baseUrl) {
        super();
        
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }
    
    public ResponseEntity<KitafinderExport> exportKitaData(String traeger, String kitaIdExtern, String password) {
        Map<String, String> requestParameters = new HashMap<>();
        requestParameters.put("csv", "n");
        requestParameters.put("benutzerId", "kita-app-eai");
        requestParameters.put("traeger", traeger);
        requestParameters.put("kitaIdExtern", kitaIdExtern);
        requestParameters.put("passwort", password);
        
        ResponseEntity<KitafinderExport> response = restTemplate.getForEntity(baseUrl, KitafinderExport.class);
                 
        return response;
    }
    
}
