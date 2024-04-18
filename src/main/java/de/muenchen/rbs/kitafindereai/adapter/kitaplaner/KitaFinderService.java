/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigDataRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Service
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
