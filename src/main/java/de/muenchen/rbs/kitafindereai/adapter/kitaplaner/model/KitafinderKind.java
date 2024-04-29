/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * A child as received from kitafinder.
 * 
 * @author m.zollbrecht
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KitafinderKind {
    
    @JsonProperty("KIND_VORNAME")
    private String kindVorname;
    @JsonProperty("KIND_NACHNAME")
    private String kindNachname;
    @JsonProperty("KIND_GEBDATUM")
    private String kindGebdatum;
    @JsonProperty("KIND_GESCHLECHT")
    private String kindGeschlecht;
    @JsonProperty("KIND_STAATSANGEHOERIGKEIT")
    private String kindStaatsangehoerigkeit;
    @JsonProperty("KIND_FAMILIENSPRACHE")
    private String kindFamiliensprache;
    @JsonProperty("KIND_KONFESSION")
    private String kindKonfession;
    @JsonProperty("KIND_ID_EXTERN")
    private String kindIdExtern;
    @JsonProperty("KIND_ID_BEWERBUNG")
    private String kindIdBewerbung;
    @JsonProperty("KIND_ID_BEWERBUNGSMAPPE")
    private String kindIdBewerbungsmappe;
    @JsonProperty("KIND_ID_KINDMAPPE")
    private String kindIdKindmappe;
    
    @JsonProperty("SB1_VORNAME")
    private String sb1Vorname;
    @JsonProperty("SB1_NACHNAME")
    private String sb1Nachname;
    @JsonProperty("SB1_GEBDATUM")
    private String sb1Gebdatum;
    @JsonProperty("SB1_ANREDE")
    private String sb1Anrede;
    @JsonProperty("SB1_TITEL")
    private String sb1Titel;
    @JsonProperty("SB1_STRASSE")
    private String sb1Strasse;
    @JsonProperty("SB1_HAUSNUMMER")
    private String sb1Hausnummer;
    @JsonProperty("SB1_ORT")
    private String sb1Ort;
    @JsonProperty("SB1_POSTLEITZAHL")
    private String sb1Postleitzahl;
    @JsonProperty("SB1_TELEFON1")
    private String sb1Telefon1;
    @JsonProperty("SB1_TELEFON2")
    private String sb1Telefon2;
    @JsonProperty("SB1_EMAIL")
    private String sb1Email;
    @JsonProperty("SB1_FAMILIENSTAND")
    private String sb1Familienstand;
    @JsonProperty("SB1_ALLEINSORGEBERECHTIGT")
    private String sb1Alleinsorgeberechtigt;
    @JsonProperty("SB2_VORNAME")
    private String sb2Vorname;
    @JsonProperty("SB2_NACHNAME")
    private String sb2Nachname;
    @JsonProperty("SB2_GEBDATUM")
    private String sb2Gebdatum;
    @JsonProperty("SB2_ANREDE")
    private String sb2Anrede;
    @JsonProperty("SB2_TITEL")
    private String sb2Titel;
    @JsonProperty("SB2_STRASSE")
    private String sb2Strasse;
    @JsonProperty("SB2_HAUSNUMMER")
    private String sb2Hausnummer;
    @JsonProperty("SB2_ORT")
    private String sb2Ort;
    @JsonProperty("SB2_POSTLEITZAHL")
    private String sb2Postleitzahl;
    @JsonProperty("SB2_TELEFON1")
    private String sb2Telefon1;
    @JsonProperty("SB2_TELEFON2")
    private String sb2Telefon2;
    @JsonProperty("SB2_EMAIL")
    private String sb2Email;
    @JsonProperty("KITA_ID")
    private String kitaId;
    @JsonProperty("KITA_ID_EXTERN")
    private String kitaIdExtern;
    @JsonProperty("KITA_KITANAME")
    private String kitaKitaname;
    @JsonProperty("KITA_KITAANSCHRIFT")
    private String kitaKitaanschrift;
    @JsonProperty("BEW_ERSTVORSTELLUNG")
    private String bewErstvorstellung;
    @JsonProperty("BEW_BETREUUNGSWUNSCH_AB")
    private String bewBetreuungswunschAb;
    @JsonProperty("BEW_VERTRAGSENDE_GEPLANT")
    private String bewVertragsendeGeplant;
    @JsonProperty("BEW_VERTRAGSENDE_SPAETESTENS")
    private String bewVertragsendeSpaetestens;
    @JsonProperty("BEW_BETREUUNGSSTUNDEN_TÄGLICH")
    private String bewBetreuungsstundenTaeglich;
    @JsonProperty("BEW_ALTERSGRUPPE_ZUM_BETREUUNGSWUNSCH_AB")
    private String bewAltersgruppeZumBetreuungswunschAb;
    @JsonProperty("BEW_STATUS")
    private String bewStatus;
    @JsonProperty("VER_VERTRAG_AB")
    private String verVertragAb;
    @JsonProperty("VER_KUENDIGUNG_ZUM")
    private String verKuendigungZum;
    @JsonProperty("VER_BETREUUNGSSTUNDEN_TÄGLICH_ZUM_EXPORTDATUM")
    private String verBetreuungsstundenTaeglichZumExportdatum;
    @JsonProperty("VER_ALTERSGRUPPE_ZUM_EXPORTDATUM")
    private String verAltersgruppeZumExportdatum;
    @JsonProperty("VER_GRUPPE")
    private String verGruppe;
    @JsonProperty("VER_MITTAGESSEN")
    private String verMittagessen;
    @JsonProperty("EXPORTDATUM")
    private String exportdatum;
    // optional fields. These fields need to be activated per traeger in kita-planer.
    @JsonProperty("WOHNHAFT_BEI")
    private String wohnhaftBei;
    @JsonProperty("ABW_VORNAME")
    private String abwVorname;
    @JsonProperty("ABW_NACHNAME")
    private String abwNachname;
    @JsonProperty("ABW_STRASSE")
    private String abwStrasse;
    @JsonProperty("ABW_HAUSNUMMER")
    private String abwHausnummer;
    @JsonProperty("ABW_PLZ")
    private String abwPlz;
    @JsonProperty("ABW_ORT")
    private String abwOrt;
    @JsonProperty("ABW_LAND")
    private String abwLand;
    @JsonProperty("VER_GRUPPE_ID")
    private String verGruppeId;
    @JsonProperty("VER_GRUPPE_AB")
    private String verGruppeAb;
}
