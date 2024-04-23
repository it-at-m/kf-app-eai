package de.muenchen.rbs.kitafindereai.api.model;

import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Institute
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Institute   {

    @NotNull
    @Size(max = 25)
    @Schema(example = "1620028207", description = "kibigWebId der Einrichtung")
    // Kitafinder-Column [KITA_ID_EXTERN]
    private String instituteId;
    
    @Size(max = 128)
    @Schema(example = "Lerchenkinderhaus e. V.", description = "Name der Einrichtung")
    // Kitafinder-Column [KITA_KITANAME]
    private String instituteName;
    
    private Collection<Group> groups;
  
}
