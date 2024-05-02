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
import de.muenchen.rbs.kitafindereai.audit.model.AuditDto;
import lombok.Getter;
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
        AuditDto auditInfo = new AuditDto();
        auditInfo.setReqKibigwebId(kibigwebId);
        KitafinderKitaKonfigData data = repository.findById(kibigwebId).orElseThrow(
                () -> {
                    log.error(String.format("Es wurden keine Einträge zur kibigWebId %s in KITA_DATA gefunden",
                            kibigwebId));
                    return new MissingKitaKonfigDataException(
                            "keine Konfigurationsdaten zu kibigwebId " + kibigwebId + " vorhanden",
                            auditInfo);
                });
        auditInfo.setRslvKitaIdExtern(data.getKitaIdExtern());
        auditInfo.setRslvTraeger(data.getTraeger());

        String decryptedPassword = encryptor.decrypt(data.getPassword());

        ResponseEntity<KitafinderExport> response = adapter.exportKitaData(
                data.getTraeger(),
                data.getKitaIdExtern(),
                decryptedPassword);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            if (response.getBody() != null && response.getBody().getStatus() == 0) {
                if (response.getBody().getAnzahlDatensaetze() > 0) {
                    KitafinderExport kitafinderExport = response.getBody();
                    kitafinderExport.setAuditDto(auditInfo);
                    return kitafinderExport;
                } else {
                    throw new NoDataException(auditInfo);
                }
            } else {
                log.error("Error in kitafinder response: " + response.getBody().getFehlermeldung());
                auditInfo.setErrorTrace(response.getBody().getFehlermeldung());
                throw new KitafinderException("Kitafinder call failed", auditInfo);
            }
        } else {
            log.error("Kitafinder call failed with status code " + response.getStatusCode().value());
            throw new KitafinderException(
                    "Kitafinder call failed with status code " + response.getStatusCode().value(), auditInfo);
        }
    }

    /**
     * Exception for missing config data, that makes calls requesting a specific kibigwebid
     * impossible.
     */
    @Getter
    public class MissingKitaKonfigDataException extends RuntimeException {
        private static final long serialVersionUID = 8425994162414148989L;

        public static String DETAILS = "Es sind in der Komponente kita-finder-eai keine Daten zu der übergebenen kibigwebid hinterlegt. Für diese Kibigwebid können daher keine Daten geliefert werden.";
        public AuditDto auditReqRslv;

        public MissingKitaKonfigDataException(String message, AuditDto auditReqRslv) {
            super(message);
            this.auditReqRslv = auditReqRslv;
        }

    }

    /**
     * Exception for errors when calling or interpreting kitafinder data.
     */
    @Getter
    public class KitafinderException extends RuntimeException {
        private static final long serialVersionUID = 1251395732301276870L;

        public static String DETAILS = "Beim Aufruf des Kitafinders ist ein Fehler aufgetreten.";
        public AuditDto auditReqRslv;

        public KitafinderException(String message, AuditDto auditReqRslv) {
            super(message);
            this.auditReqRslv = auditReqRslv;
        }
    }

    /**
     * Exception for empty kitafinder data.
     */
    @Getter
    public class NoDataException extends RuntimeException {
        private static final long serialVersionUID = -7755165370313373931L;

        public static String DETAILS = "Die Antwort des Kitafinders enthält keine Daten.";
        public AuditDto auditReqRslv;

        public NoDataException(AuditDto auditReqRslv) {
            super();
            this.auditReqRslv = auditReqRslv;
        }

    }

}
