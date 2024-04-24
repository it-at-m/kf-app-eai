/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model;

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

    private String KIND_VORNAME;
    private String KIND_NACHNAME;
    private String KIND_GEBDATUM;
    private String KIND_GESCHLECHT;
    private String KIND_STAATSANGEHOERIGKEIT;
    private String KIND_FAMILIENSPRACHE;
    private String KIND_KONFESSION;
    private String KIND_ID_EXTERN;
    private String KIND_ID_BEWERBUNG;
    private String KIND_ID_BEWERBUNGSMAPPE;
    private String KIND_ID_KINDMAPPE;
    private String SB1_VORNAME;
    private String SB1_NACHNAME;
    private String SB1_GEBDATUM;
    private String SB1_ANREDE;
    private String SB1_TITEL;
    private String SB1_STRASSE;
    private String SB1_HAUSNUMMER;
    private String SB1_ORT;
    private String SB1_POSTLEITZAHL;
    private String SB1_TELEFON1;
    private String SB1_TELEFON2;
    private String SB1_EMAIL;
    private String SB1_FAMILIENSTAND;
    private String SB1_ALLEINSORGEBERECHTIGT;
    private String SB2_VORNAME;
    private String SB2_NACHNAME;
    private String SB2_GEBDATUM;
    private String SB2_ANREDE;
    private String SB2_TITEL;
    private String SB2_STRASSE;
    private String SB2_HAUSNUMMER;
    private String SB2_ORT;
    private String SB2_POSTLEITZAHL;
    private String SB2_TELEFON1;
    private String SB2_TELEFON2;
    private String SB2_EMAIL;
    private String KITA_ID;
    private String KITA_ID_EXTERN;
    private String KITA_KITANAME;
    private String KITA_KITAANSCHRIFT;
    private String BEW_ERSTVORSTELLUNG;
    private String BEW_BETREUUNGSWUNSCH_AB;
    private String BEW_VERTRAGSENDE_GEPLANT;
    private String BEW_VERTRAGSENDE_SPAETESTENS;
    private String BEW_BETREUUNGSSTUNDEN_TÄGLICH;
    private String BEW_ALTERSGRUPPE_ZUM_BETREUUNGSWUNSCH_AB;
    private String BEW_STATUS;
    private String VER_VERTRAG_AB;
    private String VER_KUENDIGUNG_ZUM;
    private String VER_BETREUUNGSSTUNDEN_TÄGLICH_ZUM_EXPORTDATUM;
    private String VER_ALTERSGRUPPE_ZUM_EXPORTDATUM;
    private String VER_GRUPPE;
    private String VER_MITTAGESSEN;
    private String EXPORTDATUM;
    // optional fields. These fields need to be activated per traeger in kita-planer.
    private String WOHNHAFT_BEI;
    private String ABW_VORNAME;
    private String ABW_NACHNAME;
    private String ABW_STRASSE;
    private String ABW_HAUSNUMMER;
    private String ABW_PLZ;
    private String ABW_ORT;
    private String ABW_LAND;
    private String VER_GRUPPE_ID;
    private String VER_GRUPPE_AB;

}
