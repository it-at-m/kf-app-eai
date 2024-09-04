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
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKind;
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

        mapper.createTypeMap(KitafinderExport.class, Institute.class).setProvider(source -> {
            KitafinderExport export = (KitafinderExport) source.getSource();
            Institute institute = new Institute();
            Optional<KitafinderKind> anyKind = export.getAnyKind();

            if (anyKind.isEmpty()) {
                return null;
            } else {
                // get basic information
                institute.setInstituteId(anyKind.get().getKitaIdExtern());
                institute.setInstituteName(anyKind.get().getKitaKitaname());

                // map groups and add children
                List<Group> groups = new ArrayList<>();
                export.getDatensaetze().stream().forEach(kind -> {
                    String groupId = kind.getVerGruppeId();

                    Optional<Group> group = groups.stream().filter(g -> Objects.equals(groupId, g.getGroupId())).findAny();
                    if (group.isEmpty()) {
                        // Group is not present yet.
                        groups.add(new Group(groupId, kind.getVerGruppe(),
                                new ArrayList<>(List.of(mapper.map(kind, Child.class)))));
                    } else {
                        // Add to existing group
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
            String wohnhaftBei = context.getSource().getWohnhaftBei();

            ChildAddress adress = new ChildAddress();
            if (wohnhaftBei != null && "sb2".equals(wohnhaftBei.toLowerCase())) {
                adress.setCity(context.getSource().getSb2Ort());
                adress.setStreet(context.getSource().getSb2Strasse());
                adress.setStreetNo(context.getSource().getSb2Hausnummer());
                adress.setZipCode(context.getSource().getSb2Postleitzahl());
            } else if (wohnhaftBei != null && "abw".equals(wohnhaftBei.toLowerCase())) {
                adress.setCity(context.getSource().getAbwOrt());
                adress.setStreet(context.getSource().getAbwStrasse());
                adress.setStreetNo(context.getSource().getAbwHausnummer());
                adress.setZipCode(context.getSource().getAbwPlz());
            } else {
                // default to sb1
                adress.setCity(context.getSource().getSb1Ort());
                adress.setStreet(context.getSource().getSb1Strasse());
                adress.setStreetNo(context.getSource().getSb1Hausnummer());
                adress.setZipCode(context.getSource().getSb1Postleitzahl());
            }

            return adress;
        };

        Converter<KitafinderKind, Collection<Parent>> parentConverter = context -> {
            if (context == null || context.getSource() == null) {
                return null;
            }
            List<Parent> parents = new ArrayList<>();

            addParentIfExists(parents, ParentType.sb1, context.getSource().getSb1Vorname(),
                    context.getSource().getSb1Nachname());
            addParentIfExists(parents, ParentType.sb2, context.getSource().getSb2Vorname(),
                    context.getSource().getSb2Nachname());

            return parents;
        };

        mapper.createTypeMap(KitafinderKind.class, Child.class).addMappings(m -> {
            m.map(KitafinderKind::getKindIdExtern, Child::setChildId);
            m.map(KitafinderKind::getKindVorname, Child::setFirstName);
            m.map(KitafinderKind::getKindNachname, Child::setLastName);
            m.using(dateConverter).map(KitafinderKind::getKindGebdatum, Child::setBirthday);
            m.using(dateConverter).map(KitafinderKind::getVerVertragAb, Child::setCareStart);
            m.using(dateConverter).map(KitafinderKind::getVerKuendigungZum, Child::setCareEnd);
            m.using(adressConverter).map(k -> k, Child::setAddress);
            m.using(parentConverter).map(k -> k, Child::setParents);
        });

        return mapper;
    }

    private void addParentIfExists(List<Parent> parentList, ParentType type, String firstname, String lastname) {
        if (firstname == null || firstname.length() > 0 || lastname == null || lastname.length() > 0) {
            parentList.add(new Parent(type, firstname, lastname));
        }
    }
}
