package de.muenchen.rbs.kitafindereai.api;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.adapter.kitaplaner.data.KitafinderKitaKonfigDataRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@PreAuthorize("@environment.acceptsProfiles('no-security') || hasAuthority('ROLE_kf-app-eai-access')")
@SecurityRequirement(name = "InternalLogin")
@RequestMapping(path = "/internal/", produces = "application/json")
public class InternalApiController {

    @Autowired
    KitafinderKitaKonfigDataRepository repository;

    @Autowired
    TextEncryptor encryptor;

    @Autowired
    ModelMapper mapper;

    @PostMapping("kitadata/{kibigWebId}")
    @Operation(tags = { "internal" }, summary = "Setzt die Konfigdaten der EAI für eine kibigWebId.")
    public ResponseEntity<KitafinderKitaKonfigDataReadDto> saveKitafinderKitaKonfig(
            @Parameter(in = ParameterIn.PATH, description = "kibigWebId der Einrichtung für die Kinddaten abgerufen werden", required = true, schema = @Schema(type = "string", description = "KibigwebId 162(für München) - 001 (für Städtisch) - \\d (Art/Form der Einrichtung) - \\d{3} (Nummer der Einrichtung)", example = "1620018207")) @PathVariable("kibigWebId") String kibigWebId,
            @RequestBody KitafinderKitaKonfigDataWriteDto data) {
        log.info("Endpoint POST kitadata/{} was called. Updating/saving the received data...", kibigWebId);
        Optional<KitafinderKitaKonfigData> kitaKonfig = repository.findById(kibigWebId);

        // retrieve existing data or get default
        KitafinderKitaKonfigData kitaKonfigData = kitaKonfig.orElseGet(() -> {
            KitafinderKitaKonfigData defaultData = new KitafinderKitaKonfigData();
            defaultData.setKibigwebId(kibigWebId);
            return defaultData;
        });

        // Update with POSTed values
        kitaKonfigData.setPassword(encryptor.encrypt(data.getPassword()));
        kitaKonfigData.setKitaIdExtern(data.getKitaIdExtern());
        kitaKonfigData.setTraeger(data.getTraeger());
        // Add user and timestamp
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        kitaKonfigData.setLastChangedBy(authentication.getName());
        kitaKonfigData.setLastChangedAt(LocalDateTime.now());

        // save
        KitafinderKitaKonfigData savedData = repository.save(kitaKonfigData);

        // return saved data
        KitafinderKitaKonfigDataReadDto dto = mapper.map(savedData, KitafinderKitaKonfigDataReadDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("kitadata/{kibigWebId}")
    @Operation(tags = { "internal" }, summary = "Liefert die hinterlegten Konfigdaten der EAI für eine kibigWebId.")
    public ResponseEntity<KitafinderKitaKonfigDataReadDto> getKitafinderKitaKonfig(
            @Parameter(in = ParameterIn.PATH, description = "kibigWebId der Einrichtung für die Kinddaten abgerufen werden", required = true, schema = @Schema(type = "string", description = "KibigwebId 162(für München) - 001 (für Städtisch) - \\d (Art/Form der Einrichtung) - \\d{3} (Nummer der Einrichtung)", example = "1620018207")) @PathVariable("kibigWebId") String kibigWebId) {
        log.info("Endpoint GET kitadata/{} was called. Retrieving the stored data...", kibigWebId);
        Optional<KitafinderKitaKonfigData> kitaKonfig = repository.findById(kibigWebId);

        if (kitaKonfig.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            KitafinderKitaKonfigDataReadDto dto = mapper.map(kitaKonfig.get(), KitafinderKitaKonfigDataReadDto.class);
            return ResponseEntity.ok().body(dto);
        }
    }

}
