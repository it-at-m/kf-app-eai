/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKind;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKindList;
import de.muenchen.rbs.kitafindereai.api.model.Child;
import de.muenchen.rbs.kitafindereai.api.model.ChildAddress;
import de.muenchen.rbs.kitafindereai.api.model.Group;
import de.muenchen.rbs.kitafindereai.api.model.Institute;
import de.muenchen.rbs.kitafindereai.api.model.Parent;
import de.muenchen.rbs.kitafindereai.api.model.Parent.ParentType;

/**
 * Create a ModelMapper to expose as a Bean.
 * 
 * @author m.zollbrecht
 */
@Configuration
public class ModelMapperConfiguration {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd'.'MM'.'yyyy", Locale.GERMAN);

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

        Converter<String, LocalDate> dateConverter = context -> {
            if (context.getSource() == null || context.getSource().length() == 0) {
                return null;
            }
            return LocalDate.parse(context.getSource(), DATE_FORMATTER);
        };

        Converter<KitafinderKind, ChildAddress> adressConverter = context -> {
            if (context == null || context.getSource() == null) {
                return null;
            }
            // TODO: dependent on WOHNHAFT_BEI
            ChildAddress adress = new ChildAddress();
            adress.setCity(context.getSource().getSB1_ORT());
            adress.setStreet(context.getSource().getSB1_STRASSE());
            adress.setStreetNo(context.getSource().getSB1_HAUSNUMMER());
            adress.setZipCode(context.getSource().getSB1_POSTLEITZAHL());
            return adress;
        };

        Converter<KitafinderKind, Collection<Parent>> parentConverter = context -> {
            if (context == null || context.getSource() == null) {
                return null;
            }
            // TODO: add ABW
            List<Parent> parents = List.of(
                    new Parent(ParentType.sb1, context.getSource().getSB1_VORNAME(),
                            context.getSource().getSB1_NACHNAME()),
                    new Parent(ParentType.sb2, context.getSource().getSB2_VORNAME(),
                            context.getSource().getSB2_NACHNAME()));
            return parents;
        };

        mapper.createTypeMap(KitafinderKind.class, Child.class).addMappings(m -> {
            m.map(KitafinderKind::getKIND_ID_EXTERN, Child::setChildId);
            m.map(KitafinderKind::getKIND_VORNAME, Child::setFirstName);
            m.map(KitafinderKind::getKIND_NACHNAME, Child::setLastName);
            m.using(dateConverter).map(KitafinderKind::getKIND_GEBDATUM, Child::setBirthday);
            m.using(dateConverter).map(KitafinderKind::getVER_VERTRAG_AB, Child::setCareStart);
            m.using(dateConverter).map(KitafinderKind::getVER_KUENDIGUNG_ZUM, Child::setCareEnd);
            m.using(adressConverter).map(k -> k, Child::setAddress);
            m.using(parentConverter).map(k -> k, Child::setParents);
        });

        return mapper;
    }
}
