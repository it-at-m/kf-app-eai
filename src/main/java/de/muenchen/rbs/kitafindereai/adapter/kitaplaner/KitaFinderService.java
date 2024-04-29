/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigDataRepository;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to call kitafinder using stored config data in the form of
 * {@linkplain KitafinderKitaKonfigData}.
 * 
 * @author m.zollbrecht
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

    public KitafinderExport exportKitaData(String kibigwebId)
            throws MissingKitaKonfigDataException, KitafinderException {
        KitafinderKitaKonfigData data = repository.findById(kibigwebId).orElseThrow(
                () -> new MissingKitaKonfigDataException("keine Daten zu kibigwebId " + kibigwebId + " vorhanden"));

        String decryptedPassword = encryptor.decrypt(data.getPassword());

        ResponseEntity<KitafinderExport> response = adapter.exportKitaData(
                data.getTraeger(),
                data.getKitaIdExtern(),
                decryptedPassword);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            if (response.getBody() != null && response.getBody().getStatus() == 0) {
                if (response.getBody().getAnzahlDatensaetze() > 0) {
                    return response.getBody();
                } else {
                    throw new NoDataException();
                }
            } else {
                log.error("Error in kitafinder response: " + response.getBody().getFehlermeldung());
                throw new KitafinderException("Kitafinder call failed");
            }
        } else {
            log.error("Error getting kitafinder response. Status code " + response.getStatusCode().value());
            throw new KitafinderException(
                    "Kitafinder call failed with status code " + response.getStatusCode().value());
        }
    }

    /**
     * Exception for missing config data, that makes calls requesting a specific kibigwebid
     * impossible.
     */
    public class MissingKitaKonfigDataException extends RuntimeException {
        private static final long serialVersionUID = 8425994162414148989L;

        public static String DETAILS = "Es sind in der Komponente kita-finder-eai keine Daten zu der übergebenen kibigwebid hinterlegt. Für diese Kibigwebid können daher keine Daten geliefert werden.";

        public MissingKitaKonfigDataException(String message) {
            super(message);
        }
    }

    /**
     * Exception for errors when calling or interpreting kitafinder data.
     */
    public class KitafinderException extends RuntimeException {
        private static final long serialVersionUID = 1251395732301276870L;
        
        public static String DETAILS = "Beim Aufruf des Kitafinders ist ein Fehler aufgetreten.";

        public KitafinderException(String message) {
            super(message);
        }
    }

    /**
     * Exception for empty kitafinder data.
     */
    public class NoDataException extends RuntimeException {
        private static final long serialVersionUID = -7755165370313373931L;
        
        public static String DETAILS = "Die Antwort des Kitafinders enthält keine Daten.";
    }

}
