package de.muenchen.rbs.kitafindereai.api.model;

import java.util.ArrayList;
import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Group {

    @NotNull
    @Size(max = 25)
    @Schema(example = "9624", description = "kitafinder+ ID der Gruppe")
    // Kitafinder-Column [VER_GRUPPE_ID]
    private String groupId;
    
    @Size(max = 255)
    @Schema(example = "Die Mäuse", description = "Zugehörigkeit zur Kitagruppe zum Exportdatum")
    // Kitafinder-Column [VER_GRUPPE]
    private String name;
    
    private Collection<Child> children = new ArrayList<>();

}
