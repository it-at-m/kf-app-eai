/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import de.muenchen.rbs.kitafindereai.adapter.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigDataRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class KitaFinderService {

    @Autowired
    private KitaFinderApiAdapter adapter;

    @Autowired
    private KitafinderKitaKonfigDataRepository repository;

    @Autowired
    private TextEncryptor encryptor;

    public KitafinderExport exportKitaData(String kibigwebId) {
        KitafinderKitaKonfigData data = repository.findById(kibigwebId).orElseThrow(
                () -> new IllegalArgumentException("keine Daten zu kibigwebId " + kibigwebId + " vorhanden"));

        ResponseEntity<KitafinderExport> response = adapter.exportKitaData(data.getTraeger(), data.getKitaIdExtern(),
                encryptor.decrypt(data.getPassword()));

        return response.getBody();
    }

}
