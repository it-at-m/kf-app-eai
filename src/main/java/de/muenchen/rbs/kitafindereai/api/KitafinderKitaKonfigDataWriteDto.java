/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.api;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigData;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for {@link KitafinderKitaKonfigData}
 * 
 * @author m.zollbrecht
 */
@Data
public class KitafinderKitaKonfigDataWriteDto {

    // kibigwebid is part of url in this case.

    @Size(max = 255)
    private String kitaIdExtern;

    @Size(max = 255)
    private String traeger;

    @Size(max = 255)
    private String password;
}
