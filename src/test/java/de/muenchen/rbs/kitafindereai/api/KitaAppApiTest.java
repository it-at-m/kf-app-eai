package de.muenchen.rbs.kitafindereai.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderApiAdapter;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderKind;

@SpringBootTest
@ActiveProfiles({ "test", "no-security" })
class KitaAppApiTest {

    private MockMvc mockMvc;

    @MockBean
    private KitaFinderApiAdapter kitaFinderApiAdapter;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser
    void kitaAppApi() throws Exception {
        String kibigwebId = "123456789";

        KitafinderKitaKonfigDataWriteDto dto = new KitafinderKitaKonfigDataWriteDto();
        dto.setKitaIdExtern("kitafinder-id");
        dto.setPassword("test-password");
        dto.setTraeger("test-traeger");

        // konfig für einrichtung speichern
        mockMvc.perform(
                post("/internal/kitadata/{id}", kibigwebId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // kitafinder-call mocken
        KitafinderKind kind = new KitafinderKind();
        kind.setKindVorname("vorname");
        kind.setKindNachname("nachname");
        kind.setKindIdExtern("idExtern");
        kind.setKitaIdExtern("idExtern");
        kind.setKitaKitaname("kitaname");
        kind.setSb1Nachname("sb1vorname");
        kind.setSb1Vorname("sb1nachname");
        kind.setVerGruppeId("gruppenid");
        KitafinderExport exportData = new KitafinderExport(0, "ok", null, 1, List.of(kind), null);
        ResponseEntity<KitafinderExport> adapterResponse = ResponseEntity.ok(exportData);
        Mockito.when(kitaFinderApiAdapter.exportKitaData(dto.getTraeger(), dto.getKitaIdExtern(), dto.getPassword()))
                .thenReturn(adapterResponse);

        // daten für einrichtung abfragen
        mockMvc.perform(
                get("/kitaApp/v1/einrichtungen/{id}/mitGruppenUndKindern", kibigwebId)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
        // das mapping wird im ModelMapperTest geprüft

        Mockito.verify(kitaFinderApiAdapter, Mockito.times(1)).exportKitaData(Mockito.any(), Mockito.any(),
                Mockito.any());
    }

}
