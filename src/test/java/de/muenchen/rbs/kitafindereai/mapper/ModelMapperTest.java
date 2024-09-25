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
        source.setKindVorname("vorname Test");
        source.setKindNachname("nachname Test");
        source.setKindIdExtern("id extern Test");
        source.setKindGebdatum("28.10.2018");
        source.setVerVertragAb("01.09.2020");
        source.setVerKuendigungZum("31.10.2020");
        source.setSb1Ort("München");
        source.setSb1Strasse("Dingolfinger Str.");
        source.setSb1Hausnummer("21a");
        source.setSb1Postleitzahl("80559");
        source.setSb1Vorname("sb1 vorname Test");
        source.setSb1Nachname("sb1 nachname Test");
        source.setSb2Vorname("sb2 vorname Test");
        source.setSb2Nachname("sb2 nachname Test");

        Child dest = mapper.map(source, Child.class);

        assertThat(dest.getChildId()).isEqualTo(source.getKindIdExtern());
        assertThat(dest.getFirstName()).isEqualTo(source.getKindVorname());
        assertThat(dest.getBirthday())
                .isEqualTo(LocalDate.parse(source.getKindGebdatum(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareStart())
                .isEqualTo(LocalDate.parse(source.getVerVertragAb(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareEnd())
                .isEqualTo(LocalDate.parse(source.getVerKuendigungZum(), ModelMapperConfiguration.DATE_FORMATTER));
    }

    @Test
    void mapsKind_adresseAbweichend() {
        KitafinderKind source = new KitafinderKind();
        source.setKindVorname("vorname Test");
        source.setKindNachname("nachname Test");
        source.setKindIdExtern("id extern Test");
        source.setKindGebdatum("28.10.2018");
        source.setVerVertragAb("01.09.2020");
        source.setVerKuendigungZum("31.10.2020");
        source.setSb1Ort("München");
        source.setSb1Strasse("Dingolfinger Str.");
        source.setSb1Hausnummer("21a");
        source.setSb1Postleitzahl("80559");
        source.setSb1Vorname("sb1 vorname Test");
        source.setSb1Nachname("sb1 nachname Test");
        source.setSb2Vorname("sb2 vorname Test");
        source.setSb2Nachname("sb2 nachname Test");
        source.setAbwVorname("abw vorname Test");
        source.setAbwNachname("abw nachname Test");
        source.setAbwStrasse("Marienplatz");
        source.setAbwHausnummer("12");
        source.setAbwPlz("80331");
        source.setAbwOrt("München");
        source.setWohnhaftBei("ABW");

        Child dest = mapper.map(source, Child.class);

        assertThat(dest.getChildId()).isEqualTo(source.getKindIdExtern());
        assertThat(dest.getFirstName()).isEqualTo(source.getKindVorname());
        assertThat(dest.getBirthday())
                .isEqualTo(LocalDate.parse(source.getKindGebdatum(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareStart())
                .isEqualTo(LocalDate.parse(source.getVerVertragAb(), ModelMapperConfiguration.DATE_FORMATTER));
        assertThat(dest.getCareEnd())
                .isEqualTo(LocalDate.parse(source.getVerKuendigungZum(), ModelMapperConfiguration.DATE_FORMATTER));

        ChildAddress adress = dest.getAddress();

        assertThat(adress.getCity()).isEqualTo(source.getAbwOrt());
        assertThat(adress.getZipCode()).isEqualTo(source.getAbwPlz());
        assertThat(adress.getStreet()).isEqualTo(source.getAbwStrasse());
        assertThat(adress.getStreetNo()).isEqualTo(source.getAbwHausnummer());

        Collection<Parent> parents = dest.getParents();

        assertThat(parents).hasSize(2);
        assertThat(parents.stream().filter(p -> ParentType.sb1.equals(p.getParentType()))).hasSize(1);
        assertThat(parents.stream().filter(p -> ParentType.sb2.equals(p.getParentType()))).hasSize(1);
        assertThat(parents.stream().filter(p -> ParentType.abw.equals(p.getParentType()))).hasSize(0);
    }

    @Test
    void mapsKitafinderKindList() {
        KitafinderKind kind1 = new KitafinderKind();
        kind1.setKitaIdExtern("KITA-ID");
        kind1.setKitaKitaname("KITA-NAME");
        kind1.setKindVorname("vorname Test 1");
        kind1.setKindNachname("nachname Test 1");
        kind1.setVerGruppeId("gruppe id 1");
        kind1.setVerGruppe("gruppe Test 1");
        KitafinderKind kind2 = new KitafinderKind();
        kind2.setKitaIdExtern("KITA-ID");
        kind2.setKitaKitaname("KITA-NAME");
        kind2.setKindVorname("vorname Test 2");
        kind2.setKindNachname("nachname Test 2");
        kind2.setVerGruppeId("gruppe id 2");
        kind2.setVerGruppe("gruppe Test 2");
        KitafinderKind kind3 = new KitafinderKind();
        kind3.setKitaIdExtern("KITA-ID");
        kind3.setKitaKitaname("KITA-NAME");
        kind3.setKindVorname("vorname Test 3");
        kind3.setKindNachname("nachname Test 3");
        kind3.setVerGruppeId("gruppe id 2");
        kind3.setVerGruppe("gruppe Test 2");
        KitafinderKind kind4 = new KitafinderKind();
        kind4.setKitaIdExtern("KITA-ID");
        kind4.setKitaKitaname("KITA-NAME");
        kind4.setKindVorname("vorname Test 4");
        kind4.setKindNachname("nachname Test 4");
        kind4.setVerGruppeId("gruppe id 3");

        KitafinderExport source = new KitafinderExport(0, null, null, 4, List.of(kind1, kind2, kind3, kind4), null);

        Institute dest = mapper.map(source, Institute.class);

        assertThat(dest.getInstituteId()).isEqualTo("KITA-ID");
        assertThat(dest.getInstituteName()).isEqualTo("KITA-NAME");
        System.out.println(dest.getGroups().toString());
        assertThat(dest.getGroups()).hasSize(3);

        Optional<Group> group1 = dest.getGroups().stream().filter(g -> "gruppe id 1".equals(g.getGroupId())).findAny();
        assertThat(group1).isNotEmpty();
        assertThat(group1.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind1.getKindVorname());

        Optional<Group> group2 = dest.getGroups().stream().filter(g -> "gruppe id 2".equals(g.getGroupId())).findAny();
        assertThat(group2).isNotEmpty();
        assertThat(group2.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind2.getKindVorname(), kind3.getKindVorname());

        Optional<Group> group3 = dest.getGroups().stream().filter(g -> "gruppe id 3".equals(g.getGroupId())).findAny();
        assertThat(group3).isNotEmpty();
        assertThat(group3.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind4.getKindVorname());
    }
    


    @Test
    void mapsKitafinderKindListIgnoresDeletedContract() {
        KitafinderKind kind1 = new KitafinderKind();
        kind1.setKitaIdExtern("KITA-ID");
        kind1.setKitaKitaname("KITA-NAME");
        kind1.setKindVorname("vorname Test 1");
        kind1.setKindNachname("nachname Test 1");
        kind1.setVerGruppeId("gruppe id 1");
        kind1.setVerGruppe("gruppe Test 1");
        KitafinderKind kind2 = new KitafinderKind();
        kind2.setKitaIdExtern("KITA-ID");
        kind2.setKitaKitaname("KITA-NAME");
        kind2.setKindVorname("vorname Test 2");
        kind2.setKindNachname("nachname Test 2");
        kind2.setVerGruppeId("gruppe id 1");
        kind2.setVerGruppe("gruppe Test 1");
        kind2.setVerKuendigungZum("31.12.2009");
        KitafinderKind kind3 = new KitafinderKind();
        kind3.setKitaIdExtern("KITA-ID");
        kind3.setKitaKitaname("KITA-NAME");
        kind3.setKindVorname("vorname Test 3");
        kind3.setKindNachname("nachname Test 3");
        kind3.setVerGruppeId("gruppe id 1");
        kind3.setVerGruppe("gruppe Test 1");
        kind3.setVerKuendigungZum("31.12.2999");
        KitafinderKind kind4 = new KitafinderKind();
        kind4.setKitaIdExtern("KITA-ID");
        kind4.setKitaKitaname("KITA-NAME");
        kind4.setKindVorname("vorname Test 4");
        kind4.setKindNachname("nachname Test 4");
        kind4.setVerGruppeId("gruppe id 1");

        KitafinderExport source = new KitafinderExport(0, null, null, 4, List.of(kind1, kind2, kind3, kind4), null);

        Institute dest = mapper.map(source, Institute.class);

        assertThat(dest.getInstituteId()).isEqualTo("KITA-ID");
        assertThat(dest.getInstituteName()).isEqualTo("KITA-NAME");
        System.out.println(dest.getGroups().toString());
        assertThat(dest.getGroups()).hasSize(1);

        Optional<Group> group1 = dest.getGroups().stream().filter(g -> "gruppe id 1".equals(g.getGroupId())).findAny();
        assertThat(group1).isNotEmpty();
        assertThat(group1.get().getChildren().stream().map(k -> k.getFirstName()).toList())
                .containsExactlyInAnyOrder(kind1.getKindVorname(), kind3.getKindVorname(), kind4.getKindVorname());
    }

}
