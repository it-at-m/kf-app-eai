/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.rbs.kitafindereai.data;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link KitafinderKitaKonfigData}.
 * 
 * @author m.zollbrecht
 *
 */
public interface KitafinderKitaKonfigDataRepository extends CrudRepository<KitafinderKitaKonfigData, String> {

}
