package de.muenchen.rbs.kitafindereai.api;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigData;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigDataDto;
import de.muenchen.rbs.kitafindereai.data.KitafinderKitaKonfigDataRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin
@RestController
@SecurityRequirement(name = "BasicAuth")
@RequestMapping(path = "/internal/", produces = "application/json")
public class InternalApiController {

    @Autowired
    KitafinderKitaKonfigDataRepository repository;

    @Autowired
    TextEncryptor encryptor;

    @Autowired
    ModelMapper mapper;

    @PostMapping("kitadata/{kibigWebId}")
    public ResponseEntity<KitafinderKitaKonfigDataDto> saveKitafinderKitaKonfig(
            @Parameter(in = ParameterIn.PATH, description = "kibigWebId der Einrichtung für die Kinddaten abgerufen werden", required = true, schema = @Schema(type = "string", description = "KibigwebId 162(für München) - 001 (für Städtisch) - \\d (Art/Form der Einrichtung) - \\d{3} (Nummer der Einrichtung)", example = "1620018207")) @PathVariable("kibigWebId") String kibigWebId,
            @RequestBody KitafinderKitaKonfigData data) {
        if (!kibigWebId.equals(data.getKibigwebId())) {
            ResponseEntity.unprocessableEntity().build();
        }

        data.setPassword(encryptor.encrypt(data.getPassword()));
        KitafinderKitaKonfigData savedData = repository.save(data);

        KitafinderKitaKonfigDataDto dto = mapper.map(savedData, KitafinderKitaKonfigDataDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("kitadata/{kibigWebId}")
    public ResponseEntity<KitafinderKitaKonfigDataDto> getKitafinderKitaKonfig(
            @Parameter(in = ParameterIn.PATH, description = "kibigWebId der Einrichtung für die Kinddaten abgerufen werden", required = true, schema = @Schema(type = "string", description = "KibigwebId 162(für München) - 001 (für Städtisch) - \\d (Art/Form der Einrichtung) - \\d{3} (Nummer der Einrichtung)", example = "1620018207")) @PathVariable("kibigWebId") String kibigWebId) {
        Optional<KitafinderKitaKonfigData> kitaKonfig = repository.findById(kibigWebId);

        if (kitaKonfig.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            KitafinderKitaKonfigDataDto dto = mapper.map(kitaKonfig.get(), KitafinderKitaKonfigDataDto.class);
            return ResponseEntity.ok().body(dto);
        }
    }

}
