/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.data;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 
 */
@Data
public class KitafinderKitaKonfigDataDto {

    @Size(max = 255)
    private String kibigwebId;

    @Size(max = 255)
    private String kitaIdExtern;

    @Size(max = 255)
    private String traeger;
}
