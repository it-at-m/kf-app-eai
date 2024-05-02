/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.rbs.kitafindereai.config;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.KitafinderException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.MissingKitaKonfigDataException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.NoDataException;
import de.muenchen.rbs.kitafindereai.audit.AuditService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * ControllerAdvice for global handling of exceptions.
 *
 * @author m.zollbrecht
 */
@ControllerAdvice
public class ApiErrorHandlingControllerAdvice {

    @Autowired
    AuditService auditService;

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = { MissingKitaKonfigDataException.class })
    ErrorResponse onMissingKitaKonfigDataException(MissingKitaKonfigDataException e) {

        ErrorResponse response = new ErrorResponse(MissingKitaKonfigDataException.class.getSimpleName(),
                e.getMessage(),
                MissingKitaKonfigDataException.DETAILS);

        auditService.storeReqResEntrie(e.getAuditReqRslv().getReqKibigwebId(),
                e.getAuditReqRslv().getRslvKitaIdExtern(), e.auditReqRslv.getRslvTraeger(),
                HttpStatus.UNPROCESSABLE_ENTITY.toString(), response.getError(), e.getAuditReqRslv().getErrorTrace());

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { KitafinderException.class })
    ErrorResponse onKitafinderException(KitafinderException e) {

        ErrorResponse response = new ErrorResponse(KitafinderException.class.getSimpleName(),
                e.getMessage(),
                KitafinderException.DETAILS);

        auditService.storeReqResEntrie(e.getAuditReqRslv().getReqKibigwebId(),
                e.getAuditReqRslv().getRslvKitaIdExtern(), e.auditReqRslv.getRslvTraeger(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), response.getError(), e.getAuditReqRslv().getErrorTrace());

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = { NoDataException.class })
    void onNoDataExceptionn(NoDataException e) {
        auditService.storeReqResEntrie(e.getAuditReqRslv().getReqKibigwebId(),
                e.getAuditReqRslv().getRslvKitaIdExtern(), e.auditReqRslv.getRslvTraeger(),
                HttpStatus.NO_CONTENT.toString(), NoDataException.class.getSimpleName(),
                e.getAuditReqRslv().getErrorTrace());
        // empty response
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { Exception.class })
    public ErrorResponse handleAllUncaughtException(
            Exception e, WebRequest request) {

        auditService.storeReqResEntrie(extractUriTemplateVariable(request, "kibigWebId"),
                null, null,
                HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getClass().getSimpleName(),
                ExceptionUtils.getStackTrace(e));
        return new ErrorResponse(e.getClass().getSimpleName(), "Ein Unbekannter Fehler ist aufgetreten",
                "Bei der Bearbeitung der Anfrage kam es zu einem Unerwarteten Fehler");
    }

    private String extractUriTemplateVariable(WebRequest request, String variable) {
        try {
            Map<String, String> paramMap = (Map<String, String>) request
                    .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, WebRequest.SCOPE_REQUEST);
            String value = paramMap.get(variable);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    @Data
    @RequiredArgsConstructor
    public class ErrorResponse {
        private final LocalDateTime timestamp = LocalDateTime.now();
        private final String error;
        private final String message;
        private final String detail;
    }

}
