/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.data;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for {@link KitafinderKitaKonfigData}
 * 
 * @author m.zollbrecht
 */
@Data
public class KitafinderKitaKonfigDataReadDto {

    @Size(max = 255)
    private String kibigwebId;

    @Size(max = 255)
    private String kitaIdExtern;

    @Size(max = 255)
    private String traeger;
}
