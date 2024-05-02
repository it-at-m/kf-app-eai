package de.muenchen.rbs.kitafindereai.audit;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.muenchen.rbs.kitafindereai.audit.data.AuditRequestResponseDataRepository;
import de.muenchen.rbs.kitafindereai.audit.model.AuditRequestResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to store audit entries in the database
 * {@linkplain AuditRequestResponse}.
 * 
 * @author matthias.karl
 */
@Slf4j
@Service
public class AuditService {

    private AuditRequestResponseDataRepository reqResRepositroy;

    @Autowired
    public AuditService(AuditRequestResponseDataRepository reqResRepositroy) {
        super();
        this.reqResRepositroy = reqResRepositroy;
    }

    /**
     * Service to store an {@linkplain AuditRequestResponse}. Abbreviates all Strings to 255 chars
     * if they happen to be longer.
     * 
     * @param reqKibigWebId The kibigWebId requested
     * @param rslvKitaIdExtern The kitaIdExtern resolved for the requested kibigWebId (if resolve
     *            worked)
     * @param rslvTraeger The traeger resolved for the requested kibigWebId (if resolve worked)
     * @param resHttoStatusCode The Status Code the Controller responds with
     * @param resError If there is an Error: The Exception Name
     * @param resErrorDetail If there is an Error: The Error Detail of the Response
     * @param resErrorMessage If there is an Error: The Error Message of the Response
     * @param errorTrace Additional field to store some trace if needed. Should help to trace an
     *            error prior to the logs.
     */
    public void storeReqResEntrie(String reqKibigWebId, String rslvKitaIdExtern, String rslvTraeger,
            String resHttoStatusCode, String resError, String resErrorDetail, String resErrorMessage,
            String errorTrace) {
        try {
            AuditRequestResponse entrie = new AuditRequestResponse();
            entrie.setCreatedAt(LocalDateTime.now());
            entrie.setReqKibigwebId(StringUtils.abbreviate(reqKibigWebId, 255));
            entrie.setRslvKitaIdExtern(StringUtils.abbreviate(rslvKitaIdExtern, 255));
            entrie.setRslvTraeger(StringUtils.abbreviate(rslvTraeger, 255));
            entrie.setResHttpStatusCode(StringUtils.abbreviate(resHttoStatusCode, 255));
            entrie.setResError(StringUtils.abbreviate(resError, 255));
            entrie.setResErrorDetail(StringUtils.abbreviate(resErrorDetail, 255));
            entrie.setResErrorMessage(StringUtils.abbreviate(resErrorMessage, 255));
            entrie.setErrorTrace(StringUtils.abbreviate(errorTrace, 255));
            reqResRepositroy.save(entrie);
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

}
