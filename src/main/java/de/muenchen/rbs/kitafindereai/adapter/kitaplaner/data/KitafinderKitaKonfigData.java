/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data;

import java.time.LocalDateTime;

import de.muenchen.rbs.kitafindereai.api.model.Institute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stored data for {@link Institute}s. Used for calls to kitafinder.
 * 
 * @author m.zollbrecht
 */
@Entity
@Data
@Table(name = "kita_data")
@AllArgsConstructor
@NoArgsConstructor
public class KitafinderKitaKonfigData {

    @Id
    @Column
    @Size(max = 255)
    private String kibigwebId;

    @Column
    @Size(max = 255)
    private String password;

    @Column
    @Size(max = 255)
    private String kitaIdExtern;

    @Column
    @Size(max = 255)
    private String traeger;

    @Column
    private LocalDateTime lastChangedAt;

    @Column
    private String lastChangedBy;
    
    
}
