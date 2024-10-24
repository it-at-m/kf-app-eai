/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model;

import java.util.Collection;
import java.util.Optional;

import de.muenchen.rbs.kitafindereai.audit.model.AuditDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Wrapper of kitafinder-export data.
 * 
 * @author m.zollbrecht
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KitafinderExport {

    private int status;
    private String nachricht;
    private String fehlermeldung;
    private int anzahlDatensaetze;
    @Valid
    private Collection<KitafinderKind> datensaetze;

    // additional Field to transport auditinformation. Not part of the original kf response
    private AuditDto auditDto;

    public Optional<KitafinderKind> getAnyKind() {
        if (getDatensaetze() != null) {
            return getDatensaetze().stream().findAny();
        } else {
            return Optional.empty();
        }
    };

}
