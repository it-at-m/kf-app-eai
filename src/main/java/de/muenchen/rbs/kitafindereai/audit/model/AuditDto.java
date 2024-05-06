package de.muenchen.rbs.kitafindereai.audit.model;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Wrapper für Auditdaten die {@linkplain KitaFinderService} liefert.
 * 
 * @author matthias.karl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AuditDto {

    private String reqKibigwebId;
    private String rslvKitaIdExtern;
    private String rslvTraeger;
    private String errorTrace;

}
