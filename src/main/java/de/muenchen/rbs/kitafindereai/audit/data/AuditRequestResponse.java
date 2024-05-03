package de.muenchen.rbs.kitafindereai.audit.data;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Logged data. Used to store requests to this app and their responses.
 * 
 * @author matthias.karl
 */
@Entity
@Data
@Table(name = "aud_kita_app_requests")
public class AuditRequestResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    @Size(max = 255)
    private String reqKibigwebId;

    @Column
    @Size(max = 255)
    private String rslvKitaIdExtern;

    @Column
    @Size(max = 255)
    private String rslvTraeger;

    @Column
    @Size(max = 255)
    private String resHttpStatusCode;

    @Column
    @Size(max = 255)
    private String resError;

    @Column
    @Size(max = 255)
    private String errorTrace;

    @Column
    private LocalDateTime createdAt;

}