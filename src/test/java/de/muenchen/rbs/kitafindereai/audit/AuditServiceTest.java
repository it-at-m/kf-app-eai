package de.muenchen.rbs.kitafindereai.audit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import de.muenchen.rbs.kitafindereai.audit.data.AuditRequestResponseDataRepository;
import de.muenchen.rbs.kitafindereai.audit.model.AuditRequestResponse;

public class AuditServiceTest {

    AuditService sut;

    AuditRequestResponseDataRepository repository;

    @Captor
    private ArgumentCaptor<AuditRequestResponse> captor;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(AuditRequestResponseDataRepository.class);
        captor = ArgumentCaptor.forClass(AuditRequestResponse.class);

        sut = new AuditService(repository);

    }

    @Test
    public void abbreviation() {
        String above255String = RandomStringUtils.random(267, true, true);

        sut.storeReqResEntrie(above255String, above255String, above255String, above255String, above255String,
                above255String, above255String, above255String);

        Mockito.verify(repository).save(captor.capture());

        AuditRequestResponse entity = captor.getValue();

        assertEquals(255, entity.getReqKibigwebId().length());
        assertEquals(255, entity.getRslvKitaIdExtern().length());
        assertEquals(255, entity.getRslvTraeger().length());
        assertEquals(255, entity.getResHttpStatusCode().length());
        assertEquals(255, entity.getResError().length());
        assertEquals(255, entity.getResErrorDetail().length());
        assertEquals(255, entity.getResErrorMessage().length());
        assertEquals(255, entity.getErrorTrace().length());

    }

    @Test

    public void testStoreReqResEntrieException() {

        // Mocking an exception to be thrown by the repository
        Mockito.doThrow(new RuntimeException()).when(repository).save(any());

        // Assert that no exception is thrown by the storeReqResEntrie method
        assertDoesNotThrow(() -> sut.storeReqResEntrie("reqKibigWebId", "rslvKitaIdExtern", "rslvTraeger",
                "resHttoStatusCode", "resError", "resErrorDetail", "resErrorMessage", "errorTrace"));

        verify(repository).save(any());

    }

}
