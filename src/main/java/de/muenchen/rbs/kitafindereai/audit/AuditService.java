package de.muenchen.rbs.kitafindereai.audit;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.muenchen.rbs.kitafindereai.audit.data.AuditRequestResponse;
import de.muenchen.rbs.kitafindereai.audit.data.AuditRequestResponseDataRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to store audit entrys in the database
 * {@linkplain AuditRequestResponse}.
 * 
 * @author matthias.karl
 */
@Slf4j
@Service
public class AuditService {

    private AuditRequestResponseDataRepository reqResRepository;

    @Autowired
    public AuditService(AuditRequestResponseDataRepository reqResRepositroy) {
        super();
        this.reqResRepository = reqResRepositroy;
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
     * @param errorTrace Additional field to store some trace if needed. Should help to trace an
     *            error prior to the logs.
     */
    public void storeReqResentry(String reqKibigWebId, String rslvKitaIdExtern, String rslvTraeger,
            String resHttoStatusCode, String resError,
            String errorTrace) {
        try {
            AuditRequestResponse entry = new AuditRequestResponse();
            entry.setCreatedAt(LocalDateTime.now());
            entry.setReqKibigwebId(StringUtils.abbreviate(reqKibigWebId, 255));
            entry.setRslvKitaIdExtern(StringUtils.abbreviate(rslvKitaIdExtern, 255));
            entry.setRslvTraeger(StringUtils.abbreviate(rslvTraeger, 255));
            entry.setResHttpStatusCode(StringUtils.abbreviate(resHttoStatusCode, 255));
            entry.setResError(StringUtils.abbreviate(resError, 255));
            entry.setErrorTrace(StringUtils.abbreviate(errorTrace, 2000));
            reqResRepository.save(entry);
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

}
