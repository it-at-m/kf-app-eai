/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model;

import java.util.Collection;
import java.util.Optional;

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
    private Collection<KitafinderKind> datensaetze;

    public Optional<KitafinderKind> getAnyKind() {
        if (getDatensaetze() != null) {
            return getDatensaetze().stream().findAny();
        } else {
            return Optional.empty();
        }
    };

}
