package de.muenchen.rbs.kitafindereai.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Adress
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChildAddress   {

    @Size(max = 255)
    @Schema(example = "Thaddäus-Robl-Str.", description = "Strasse des Kindes")
    // Kitafinder-Column [SB1_STRASSE | SB2_STRASSE | ABW_STRASSE]
    private String street;
    
    @Size(max = 10)
    @Schema(example = "21a", description = "Hausnummer des Kindes")
    // Kitafinder-Column [SB1_HAUSNUMMER | SB2_HAUSNUMMER | ABW_HAUSNUMMER]
    private String streetNo;
    
    @Size(max = 255)
    @Schema(example = "München", description = "Wohnort des Kindes")
    // Kitafinder-Column [SB1_ORT | SB2_ORT | ABW_ORT]
    private String city;
    
    @Size(max = 255)
    @Schema(example = "80935", description = "Postleitzahl des Wohnortes des Kindes ")
    // Kitafinder-Column [SB1_POSTLEITZAHL | SB2_POSTLEITZAHL | ABW_POSTLEITZAHL]
    private String zipCode;
  
}
