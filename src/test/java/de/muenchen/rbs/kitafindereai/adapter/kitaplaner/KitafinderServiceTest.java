package de.muenchen.rbs.kitafindereai.adapter.kitaplaner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.KitafinderException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.MissingKitaKonfigDataException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.NoDataException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigDataRepository;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import jakarta.validation.Validator;

/**
 * Tests for {@link KitaFinderService}
 * 
 * @author m.zollbrecht
 */
class KitafinderServiceTest {

    private KitaFinderApiAdapter adapter;
    private KitafinderKitaKonfigDataRepository repository;
    private TextEncryptor encryptor;
    private Validator validator;

    private KitaFinderService service;

    @BeforeEach
    void setup() {
        adapter = Mockito.mock(KitaFinderApiAdapter.class);
        repository = Mockito.mock(KitafinderKitaKonfigDataRepository.class);
        encryptor = Mockito.mock(TextEncryptor.class);
        validator = Mockito.mock(Validator.class);

        service = new KitaFinderService(adapter, repository, encryptor, validator);
    }

    @Test
    void exportKitaData_null() {
        assertThrows(MissingKitaKonfigDataException.class, () -> service.exportKitaData(null));

        Mockito.verify(repository).findById(null);
    }

    @Test
    void exportKitaData_noData() {
        String kibigwebid = "123456789";

        KitafinderKitaKonfigData kitaKonfig = new KitafinderKitaKonfigData(kibigwebid, "testpw_encrypted", "id-extern",
                "testtraeger", LocalDateTime.now().minusDays(10), "testuser");

        KitafinderExport export = new KitafinderExport(0, "ok", null, 0, null, null);
        ResponseEntity<KitafinderExport> adapterResponse = ResponseEntity.ok(export);

        Mockito.when(repository.findById(kibigwebid)).thenReturn(Optional.of(kitaKonfig));
        Mockito.when(adapter.exportKitaData(any(), any(), any())).thenReturn(adapterResponse);
        Mockito.when(encryptor.decrypt(kitaKonfig.getPassword())).thenReturn("testpw");

        assertThrows(NoDataException.class, () -> service.exportKitaData(kibigwebid));
        
        Mockito.verify(encryptor, Mockito.times(1)).decrypt(kitaKonfig.getPassword());
        Mockito.verify(repository, Mockito.times(1)).findById(kibigwebid);
        Mockito.verify(adapter, Mockito.times(1)).exportKitaData(kitaKonfig.getTraeger(), kitaKonfig.getKitaIdExtern(), "testpw");
    }

    @Test
    void exportKitaData_kitafinderError() {
        String kibigwebid = "123456789";

        KitafinderKitaKonfigData kitaKonfig = new KitafinderKitaKonfigData(kibigwebid, "testpw_encrypted", "id-extern",
                "testtraeger", LocalDateTime.now().minusDays(10), "testuser");

        KitafinderExport export = new KitafinderExport(1, "Fehler", "Fehler", 0, null, null);
        ResponseEntity<KitafinderExport> adapterResponse = ResponseEntity.ok(export);

        Mockito.when(repository.findById(kibigwebid)).thenReturn(Optional.of(kitaKonfig));
        Mockito.when(adapter.exportKitaData(any(), any(), any())).thenReturn(adapterResponse);
        Mockito.when(encryptor.decrypt(kitaKonfig.getPassword())).thenReturn("testpw");

        assertThrows(KitafinderException.class, () -> service.exportKitaData(kibigwebid));

        Mockito.verify(encryptor, Mockito.times(1)).decrypt(kitaKonfig.getPassword());
        Mockito.verify(repository, Mockito.times(1)).findById(kibigwebid);
        Mockito.verify(adapter, Mockito.times(1)).exportKitaData(kitaKonfig.getTraeger(), kitaKonfig.getKitaIdExtern(), "testpw");
    }

    @Test
    void exportKitaData_kitafinderHttpError() {
        String kibigwebid = "123456789";

        KitafinderKitaKonfigData kitaKonfig = new KitafinderKitaKonfigData(kibigwebid, "testpw_encrypted", "id-extern",
                "testtraeger", LocalDateTime.now().minusDays(10), "testuser");

        ResponseEntity<KitafinderExport> adapterResponse = ResponseEntity.internalServerError().build();

        Mockito.when(repository.findById(kibigwebid)).thenReturn(Optional.of(kitaKonfig));
        Mockito.when(adapter.exportKitaData(any(), any(), any())).thenReturn(adapterResponse);
        Mockito.when(encryptor.decrypt(kitaKonfig.getPassword())).thenReturn("testpw");

        assertThrows(KitafinderException.class, () -> service.exportKitaData(kibigwebid));

        Mockito.verify(encryptor, Mockito.times(1)).decrypt(kitaKonfig.getPassword());
        Mockito.verify(repository, Mockito.times(1)).findById(kibigwebid);
        Mockito.verify(adapter, Mockito.times(1)).exportKitaData(kitaKonfig.getTraeger(), kitaKonfig.getKitaIdExtern(), "testpw");
    }

    @Test
    void exportKitaData_ok() {
        String kibigwebid = "123456789";

        KitafinderKitaKonfigData kitaKonfig = new KitafinderKitaKonfigData(kibigwebid, "testpw_encrypted", "id-extern",
                "testtraeger", LocalDateTime.now().minusDays(10), "testuser");

        KitafinderExport export = new KitafinderExport(0, "ok", null, 1, null, null);
        ResponseEntity<KitafinderExport> adapterResponse = ResponseEntity.ok(export);

        Mockito.when(repository.findById(kibigwebid)).thenReturn(Optional.of(kitaKonfig));
        Mockito.when(adapter.exportKitaData(any(), any(), any())).thenReturn(adapterResponse);
        Mockito.when(encryptor.decrypt(kitaKonfig.getPassword())).thenReturn("testpw");

        KitafinderExport result = service.exportKitaData(kibigwebid);

        Mockito.verify(validator, Mockito.times(1)).validate(export);
        Mockito.verify(encryptor, Mockito.times(1)).decrypt(kitaKonfig.getPassword());
        Mockito.verify(repository, Mockito.times(1)).findById(kibigwebid);
        Mockito.verify(adapter, Mockito.times(1)).exportKitaData(kitaKonfig.getTraeger(), kitaKonfig.getKitaIdExtern(), "testpw");
        assertThat(result.getAuditDto()).isNotNull();
    }

}
