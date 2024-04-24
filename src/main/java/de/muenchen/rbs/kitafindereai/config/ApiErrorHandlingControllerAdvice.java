/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.rbs.kitafindereai.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.KitafinderException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.MissingKitaKonfigDataException;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService.NoDataException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * ControllerAdvice for global handling of exceptions.
 *
 * @author m.zollbrecht
 */
@ControllerAdvice
public class ApiErrorHandlingControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = { MissingKitaKonfigDataException.class })
    ErrorResponse onMissingKitaKonfigDataException(MissingKitaKonfigDataException e) {
        ErrorResponse response = new ErrorResponse(MissingKitaKonfigDataException.class.getSimpleName(),
                e.getMessage(),
                MissingKitaKonfigDataException.DETAILS);
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { KitafinderException.class })
    ErrorResponse onKitafinderException(KitafinderException e) {
        ErrorResponse response = new ErrorResponse(KitafinderException.class.getSimpleName(),
                e.getMessage(),
                KitafinderException.DETAILS);
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(value = { NoDataException.class })
    void onNoDataExceptionn(NoDataException e) {
        // empty response
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
