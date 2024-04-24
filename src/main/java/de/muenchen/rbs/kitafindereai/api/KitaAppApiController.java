package de.muenchen.rbs.kitafindereai.api;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.KitaFinderService;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.model.KitafinderExport;
import de.muenchen.rbs.kitafindereai.api.model.Institute;
import de.muenchen.rbs.kitafindereai.config.ApiErrorHandlingControllerAdvice.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@SecurityRequirement(name = "ApiClient")
@RequestMapping(path = "/kitaApp/v1", produces = "application/json")
public class KitaAppApiController {

    @Autowired
    private KitaFinderService kitaFinderService;

    @Autowired
    private ModelMapper mapper;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(schema = @Schema(implementation = Institute.class)) }),
            @ApiResponse(responseCode = "204", description = "no content", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "403", description = "forbidden", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "404", description = "not found", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "422", description = "kitafinder error", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "4XX", description = "Sonstiger Client Fehler (4xx)", content = {
                    @Content(schema = @Schema(implementation = Void.class)) }),
            @ApiResponse(responseCode = "5XX", description = "Server Fehler (5xx)", content = {
                    @Content(schema = @Schema(implementation = Void.class)) })
    })
    @Operation(tags = {
            "kinder" }, summary = "Liefert Kinddaten", description = "Liefert Kinddaten einer Einrichtung anhand der kibigWebId in Gruppen gruppiert.", operationId = "getGroupsWithKidsByKibigwebid")
    @GetMapping("einrichtungen/{kibigWebId}/mitGruppenUndKindern")
    public ResponseEntity<Institute> getGroupsWithKidsByKibigwebid(
            @Parameter(in = ParameterIn.PATH, description = "kibigWebId der Einrichtung für die Kinddaten abgerufen werden", required = true, schema = @Schema(type = "string", description = "KibigwebId 162(für München) - 001 (für Städtisch) - \\d (Art/Form der Einrichtung) - \\d{3} (Nummer der Einrichtung)", example = "1620018207")) @PathVariable("kibigWebId") String kibigWebId) {
        log.info("Endpoint GET einrichtungen/{}/mitGruppenUndKindern called.", kibigWebId);

        KitafinderExport export = kitaFinderService.exportKitaData(kibigWebId);
        Institute institute = mapper.map(export.getDatensaetze(), Institute.class);
        return new ResponseEntity<Institute>(institute, HttpStatus.OK);

    }

}
