/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.rbs.kitafindereai.audit.data;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.muenchen.rbs.kitafindereai.audit.model.AuditRequestResponse;

/**
 * Repository for {@link AuditRequestResponse}.
 * 
 * @author matthias.karl
 *
 */
public interface AuditRequestResponseDataRepository extends CrudRepository<AuditRequestResponse, UUID> {

}
