/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Parent
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Parent {
    
    @NotNull
    private ParentType parentType;
    
    @NotNull
    @Size(max = 255)
    @Schema(example = "Astrid", description = "Vorname des Sorgeberechtigten")
    // Kitafinder-Column [SB1_VORNAME | SB2_VORNAME | ABW_VORNAME]
    private String firstName;
    
    @NotNull
    @Size(max = 255)
    @Schema(example = "Lustig", description = "Nachname des Sorgeberechtigten")
    // Kitafinder-Column [SB1_NACHNAME | SB2_NACHNAME | ABW_NACHNAME]
    private String lastName;
    
    public enum ParentType {
        sb1, sb2, abw;
    }

}
