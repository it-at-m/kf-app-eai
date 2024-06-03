package de.muenchen.rbs.kitafindereai.api.model;

import java.time.LocalDate;
import java.util.Collection;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Child
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Child   {
    
    @NotNull
    @Size(max = 25)
    @Schema(example = "Stark", description = "Dieses Feld wird bei Bewerbungen vom erfassenden System, also vom Kita-Planer 2 selbst, vergeben und zur eindeutigen Identifizierung der Bewerbung/des Vertrags genutzt.Es handelt sich NICHT um eine ID des physischen Kindes. Die KIND_ID_EXTERN ist eindeutig für das Tripel: Kind, Bewerbungsprozess, Kita. Pro Kita und Bewerbungsprozess (z.B. andere Altersgruppe) gibt es also für das gleiche physische Kind eine unterschiedliche externe ID.")
    // Kitafinder-Column [KIND_ID_EXTERN]
    private String childId;
    
    @NotNull
    @Size(max = 128)
    @Schema(example = "Tine", description = "Vorname des Kindes")
    // Kitafinder-Column [KIND_VORNAME]
    private String firstName;
    
    @NotNull
    @Size(max = 128)
    @Schema(example = "Stark", description = "Nachname des Kindes")
    // Kitafinder-Column [KIND_NACHNAME]
    private String lastName;

    @Schema(example = "2009-10-07", description = "Geburtstag des Kindes")
    // Kitafinder-Column [KIND_GEBURTSDATUM]
    private LocalDate birthday;

    @Schema(example = "2012-08-01", description = "Datum des Vertragsbeginns bzw. der aktuellen Vertragsänderung")
    // Kitafinder-Column [VER_VERTRAG_AB]
    private LocalDate careStart;

    @Schema(example = "2012-09-30", description = "Datum zu dem der Vertrag gekündigt ist (wenn explizit gekündigt wurde)")
    // Kitafinder-Column [VER_KUENDIGUNG_ZUM]
    private LocalDate careEnd;

    @Schema(description = "Adresse des Kindes. Wird aus der Adresse der SB1 | SB2 bzw. der abweichenden (ABW) Adresse berechnet. Berechnung abhängig von")
    // Kitafinder-Column [WOHNHAFT_BEI]
    private ChildAddress address;

    @Schema(description = "Array mit Sorgeberechtigten für das Kind. Liste wird aus SB1, SB2 und ABW berechnet.")
    private Collection<Parent> parents;

}
