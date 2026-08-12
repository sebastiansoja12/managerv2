package com.warehouse.tracking.infrastructure.adapter.primary;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.TrackingIntegrationConfiguration;
import com.warehouse.tracking.domain.service.TrackingIntegrationService;
import com.warehouse.tracking.domain.service.TrackingProviderRegistry;
import com.warehouse.tracking.infrastructure.adapter.primary.api.ExternalTrackingApiResponse;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingIntegrationApiRequest;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingIntegrationApiResponse;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingProviderApiResponse;
import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingSearchApiRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tracking")
@AccessUserControl
public class TrackingController {

    private final TrackingProviderRegistry providerRegistry;
    private final TrackingIntegrationService integrationService;

    public TrackingController(final TrackingProviderRegistry providerRegistry,
                              final TrackingIntegrationService integrationService) {
        this.providerRegistry = providerRegistry;
        this.integrationService = integrationService;
    }

    @GetMapping("/providers")
    public ResponseEntity<List<TrackingProviderApiResponse>> availableProviders() {
        return ResponseEntity.ok(providerRegistry.availableProviders().stream()
                .map(TrackingProviderApiResponse::from)
                .toList());
    }

    @PostMapping("/search")
    public ResponseEntity<List<ExternalTrackingApiResponse>> search(
            @Valid @RequestBody final TrackingSearchApiRequest request) {
        return ResponseEntity.ok(providerRegistry.track(request.provider(), request.trackingNumbers()).stream()
                .map(ExternalTrackingApiResponse::from)
                .toList());
    }

    @GetMapping("/integrations")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<List<TrackingIntegrationApiResponse>> integrations() {
        return ResponseEntity.ok(providerRegistry.integrationDefinitions().stream()
                .map(definition -> TrackingIntegrationApiResponse.from(definition,
                        integrationService.find(definition.provider()).orElse(null)))
                .toList());
    }

    @GetMapping("/integrations/{providerId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_READ, UserPermission.ROLE_MANAGER_READ})
    public ResponseEntity<TrackingIntegrationApiResponse> integration(
            @PathVariable final TrackingProviderId providerId) {
        final TrackingIntegrationConfiguration configuration = integrationService.find(providerId).orElse(null);
        return ResponseEntity.ok(TrackingIntegrationApiResponse.from(
                providerRegistry.integrationDefinition(providerId), configuration));
    }

    @PutMapping("/integrations/{providerId}")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<Void> saveIntegration(@PathVariable final TrackingProviderId providerId,
                                                @Valid @RequestBody final TrackingIntegrationApiRequest request) {
        integrationService.save(providerId, request.enabled(), request.values());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/integrations/{providerId}/test")
    @AccessUserControl(permissions = {UserPermission.ROLE_ADMIN_UPDATE, UserPermission.ROLE_MANAGER_UPDATE})
    public ResponseEntity<Void> testIntegration(
            @PathVariable final TrackingProviderId providerId,
            @Valid @RequestBody final TrackingIntegrationApiRequest request) {
        integrationService.testConnection(providerId, request.values());
        return ResponseEntity.noContent().build();
    }
}
