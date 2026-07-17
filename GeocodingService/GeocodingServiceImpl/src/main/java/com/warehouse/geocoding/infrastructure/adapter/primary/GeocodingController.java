package com.warehouse.geocoding.infrastructure.adapter.primary;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.GeocodingConfigurationId;
import com.warehouse.geocoding.domain.model.GeocodingConfiguration;
import com.warehouse.geocoding.domain.port.primary.GeocodingPort;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiRequest;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingConfigurationApiResponse;
import com.warehouse.geocoding.infrastructure.adapter.primary.api.GeocodingProviderApiResponse;
import com.warehouse.geocoding.infrastructure.adapter.primary.mapper.GeocodingConfigurationApiMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/geocoding-configurations")
public class GeocodingController {

    private final GeocodingPort geocodingPort;

    public GeocodingController(final GeocodingPort geocodingPort) {
        this.geocodingPort = geocodingPort;
    }

    @PostMapping
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_CREATE, UserPermission.ROLE_MANAGER_CREATE})
    public ResponseEntity<?> create(
            @Valid @RequestBody final GeocodingConfigurationApiRequest request) {
        this.geocodingPort.create(
                GeocodingConfigurationApiMapper.toCreateCommand(request));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<GeocodingConfigurationApiResponse>> getAll() {
        return ResponseEntity.ok(geocodingPort.getAll()
                .stream()
                .map(GeocodingConfigurationApiMapper::toResponse)
                .toList());
    }

    @GetMapping("/providers")
    public ResponseEntity<List<GeocodingProviderApiResponse>> getProviders() {
        return ResponseEntity.ok(Arrays.stream(GeocodingProvider.values())
                .map(GeocodingProviderApiResponse::from)
                .toList());
    }

    @GetMapping("/{geocodingConfigurationId}")
    public ResponseEntity<GeocodingConfigurationApiResponse> get(
            @PathVariable final UUID geocodingConfigurationId) {
        final GeocodingConfiguration configuration = geocodingPort.get(
                new GeocodingConfigurationId(geocodingConfigurationId));
        return ResponseEntity.ok(GeocodingConfigurationApiMapper.toResponse(configuration));
    }

    @PutMapping("/{geocodingConfigurationId}")
    public ResponseEntity<Void> update(
            @PathVariable final UUID geocodingConfigurationId,
            @Valid @RequestBody final GeocodingConfigurationApiRequest request) {
        final GeocodingConfigurationId configurationId = new GeocodingConfigurationId(geocodingConfigurationId);
        geocodingPort.update(GeocodingConfigurationApiMapper.toUpdateCommand(configurationId, request));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{geocodingConfigurationId}")
    public ResponseEntity<Void> delete(@PathVariable final UUID geocodingConfigurationId) {
        geocodingPort.delete(new GeocodingConfigurationId(geocodingConfigurationId));
        return ResponseEntity.noContent().build();
    }
}
