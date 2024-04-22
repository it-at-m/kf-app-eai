/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKind;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKindList;
import de.muenchen.rbs.kitafindereai.api.model.Child;
import de.muenchen.rbs.kitafindereai.api.model.Group;
import de.muenchen.rbs.kitafindereai.api.model.Institute;

/**
 * Create a ModelMapper to expose as a Bean.
 */
@Configuration
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(KitafinderKindList.class, Institute.class).setProvider(source -> {
            KitafinderKindList export = (KitafinderKindList) source.getSource();
            Institute institute = new Institute();
            Optional<KitafinderKind> anyKind = export.getAnyKind();

            if (anyKind.isEmpty()) {
                return null;
            } else {
                // get basic information
                institute.setInstituteId(anyKind.get().getKITA_ID());
                institute.setInstituteId(anyKind.get().getKITA_KITANAME());

                // map groups and add children
                List<Group> groups = new ArrayList<>();
                export.getDatensatz().stream().forEach(kind -> {
                    String groupName = kind.getVER_GRUPPE();
                    Optional<Group> group = groups.stream().filter(g -> g.getName().equals(groupName)).findAny();
                    if (group.isEmpty()) {
                        // Group is not present yet.
                        groups.add(new Group(null, groupName, new ArrayList<>(List.of(mapper.map(kind, Child.class)))));
                    } else {
                        group.get().getChildren().add(mapper.map(kind, Child.class));
                    }
                });
                institute.setGroups(groups);

                return institute;
            }
        });

        mapper.createTypeMap(KitafinderKind.class, Child.class).addMappings(m -> {
            m.map(KitafinderKind::getKIND_VORNAME, Child::setFirstName);
            m.map(KitafinderKind::getKIND_NACHNAME, Child::setLastName);
            // TODO remaining props
        });

        return mapper;
    }
}
