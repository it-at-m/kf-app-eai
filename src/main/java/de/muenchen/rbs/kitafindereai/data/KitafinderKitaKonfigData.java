/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 
 */
@Entity
@Data
@Table(name = "kitaData")
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
}
