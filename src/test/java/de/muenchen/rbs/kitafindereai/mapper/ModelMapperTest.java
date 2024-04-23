package de.muenchen.rbs.kitafindereai.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKind;
import de.muenchen.rbs.kitafindereai.api.model.Child;
import de.muenchen.rbs.kitafindereai.api.model.ChildAddress;
import de.muenchen.rbs.kitafindereai.api.model.Group;
import de.muenchen.rbs.kitafindereai.api.model.Institute;
import de.muenchen.rbs.kitafindereai.api.model.Parent;
import de.muenchen.rbs.kitafindereai.api.model.Parent.ParentType;
import de.muenchen.rbs.kitafindereai.config.ModelMapperConfiguration;

/**
 * Tests for {@link ModelMapperConfiguration}
 * 
 * @author m.zollbrecht
 */
class ModelMapperTest {

    private ModelMapper mapper = new ModelMapperConfiguration().modelMapper();

    @Test
    void mapsKind() {
        KitafinderKind source = new KitafinderKind();
        source.setKIND_VORNAME("vorname Test");
        source.setKIND_NACHNAME("nachname Test");
        source.setKIND_ID_EXTERN("id extern Test");
        source.setKIND_GEBDATUM("28.10.2018");
        source.setVER_VERTRAG_AB("01.09.2020");
        source.setVER_KUENDIGUNG_ZUM("31.10.2020");
        source.setSB1_ORT("München");
        source.setSB1_STRASSE("Dingolfinger Str.");
        source.setSB1_HAUSNUMMER("21a");
        source.setSB1_POSTLEITZAHL("80559");
        source.setSB1_VORNAME("sb1 vorname Test");
        source.setSB1_NACHNAME("sb1 nachname Test");
        source.setSB2_VORNAME("sb2 vorname Test");
        source.setSB2_NACHNAME("sb2 nachname Test");

        Child dest = mapper.map(source, Child.class);

        assertThat(dest.getChildId()).isEqualTo(source.getKIND_ID_EXTERN());
        assertThat(dest.getFirstName()).isEqualTo(source.getKIND_VORNAME());
        assertThat(dest.getBirthday())
                .isEqualTo(LocalDate.parse(source.getKIND_GEBDATUM(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareStart())
                .isEqualTo(LocalDate.parse(source.getVER_VERTRAG_AB(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareEnd())
                .isEqualTo(LocalDate.parse(source.getVER_KUENDIGUNG_ZUM(), ModelMapperConfiguration.DATE_FORMATTER));
    }

    @Test
    void mapsKind_adresseAbweichend() {
        KitafinderKind source = new KitafinderKind();
        source.setKIND_VORNAME("vorname Test");
        source.setKIND_NACHNAME("nachname Test");
        source.setKIND_ID_EXTERN("id extern Test");
        source.setKIND_GEBDATUM("28.10.2018");
        source.setVER_VERTRAG_AB("01.09.2020");
        source.setVER_KUENDIGUNG_ZUM("31.10.2020");
        source.setSB1_ORT("München");
        source.setSB1_STRASSE("Dingolfinger Str.");
        source.setSB1_HAUSNUMMER("21a");
        source.setSB1_POSTLEITZAHL("80559");
        source.setSB1_VORNAME("sb1 vorname Test");
        source.setSB1_NACHNAME("sb1 nachname Test");
        source.setSB2_VORNAME("sb2 vorname Test");
        source.setSB2_NACHNAME("sb2 nachname Test");
        source.setABW_VORNAME("abw vorname Test");
        source.setABW_NACHNAME("abw nachname Test");
        source.setABW_STRASSE("Marienplatz");
        source.setABW_HAUSNUMMER("12");
        source.setABW_PLZ("80331");
        source.setABW_ORT("München");
        source.setWOHNHAFT_BEI("ABW");

        Child dest = mapper.map(source, Child.class);

        assertThat(dest.getChildId()).isEqualTo(source.getKIND_ID_EXTERN());
        assertThat(dest.getFirstName()).isEqualTo(source.getKIND_VORNAME());
        assertThat(dest.getBirthday())
                .isEqualTo(LocalDate.parse(source.getKIND_GEBDATUM(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareStart())
                .isEqualTo(LocalDate.parse(source.getVER_VERTRAG_AB(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareEnd())
                .isEqualTo(LocalDate.parse(source.getVER_KUENDIGUNG_ZUM(), ModelMapperConfiguration.DATE_FORMATTER));

        ChildAddress adress = dest.getAddress();

        assertThat(adress.getCity()).isEqualTo(source.getABW_ORT());
        assertThat(adress.getZipCode()).isEqualTo(source.getABW_PLZ());
        assertThat(adress.getStreet()).isEqualTo(source.getABW_STRASSE());
        assertThat(adress.getStreetNo()).isEqualTo(source.getABW_HAUSNUMMER());

        Collection<Parent> parents = dest.getParents();

        assertThat(parents).hasSize(3);
        assertThat(parents.stream().filter(p -> ParentType.sb1.equals(p.getParentType()))).hasSize(1);
        assertThat(parents.stream().filter(p -> ParentType.sb2.equals(p.getParentType()))).hasSize(1);
        assertThat(parents.stream().filter(p -> ParentType.abw.equals(p.getParentType()))).hasSize(1);
    }

    @Test
    void mapsKitafinderKindList() {
        KitafinderKind kind1 = new KitafinderKind();
        kind1.setKITA_ID_EXTERN("KITA-ID");
        kind1.setKITA_KITANAME("KITA-NAME");
        kind1.setKIND_VORNAME("vorname Test 1");
        kind1.setKIND_NACHNAME("nachname Test 1");
        kind1.setVER_GRUPPE("gruppe Test 1");

        KitafinderKind kind2 = new KitafinderKind();
        kind2.setKITA_ID_EXTERN("KITA-ID");
        kind2.setKITA_KITANAME("KITA-NAME");
        kind2.setKIND_VORNAME("vorname Test 2");
        kind2.setKIND_NACHNAME("nachname Test 2");
        kind2.setVER_GRUPPE("gruppe Test 2");

        KitafinderKind kind3 = new KitafinderKind();
        kind3.setKITA_ID_EXTERN("KITA-ID");
        kind3.setKITA_KITANAME("KITA-NAME");
        kind3.setKIND_VORNAME("vorname Test 3");
        kind3.setKIND_NACHNAME("nachname Test 3");
        kind3.setVER_GRUPPE("gruppe Test 2");

        KitafinderExport source = new KitafinderExport(0, null, null, 3, List.of(kind1, kind2, kind3));

        Institute dest = mapper.map(source, Institute.class);

        assertThat(dest.getInstituteId()).isEqualTo("KITA-ID");
        assertThat(dest.getInstituteName()).isEqualTo("KITA-NAME");
        assertThat(dest.getGroups()).hasSize(2);

        Optional<Group> group1 = dest.getGroups().stream().filter(g -> "gruppe Test 1".equals(g.getName())).findAny();
        assertThat(group1).isNotEmpty();
        assertThat(group1.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind1.getKIND_VORNAME());

        Optional<Group> group2 = dest.getGroups().stream().filter(g -> "gruppe Test 2".equals(g.getName())).findAny();
        assertThat(group2).isNotEmpty();
        assertThat(group2.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind2.getKIND_VORNAME(), kind3.getKIND_VORNAME());
    }

}
