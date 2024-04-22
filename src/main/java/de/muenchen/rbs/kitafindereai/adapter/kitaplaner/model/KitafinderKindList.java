/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model;

import java.util.List;
import java.util.Optional;

import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@XmlType
public class KitafinderKindList {

    private List<KitafinderKind> datensatz;

    public Optional<KitafinderKind> getAnyKind() {
        if (getDatensatz() != null) {
            return getDatensatz().stream().findAny();
        } else {
            return Optional.empty();
        }
    };

}
