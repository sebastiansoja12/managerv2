package com.warehouse.platformadministration.infrastructure.adapter.primary;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.platformadministration.application.port.primary.PlatformOwnerAccountPort;
import com.warehouse.platformadministration.application.port.primary.command.CreatePlatformOwnerAccountCommand;
import com.warehouse.platformadministration.domain.exception.PlatformActorRequiredException;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ChangePlatformOwnerPasswordRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.CreatePlatformOwnerRequest;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PlatformOwnerAccountResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.ValueResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.mapper.PlatformAdministrationResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/platform/owners")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class PlatformOwnerAccountController {

    private final PlatformOwnerAccountPort platformOwnerAccountPort;

    public PlatformOwnerAccountController(final PlatformOwnerAccountPort platformOwnerAccountPort) {
        this.platformOwnerAccountPort = platformOwnerAccountPort;
    }

    @GetMapping
    public ResponseEntity<List<PlatformOwnerAccountResponse>> findAll() {
        return ResponseEntity.ok(platformOwnerAccountPort.findAll()
                .stream()
                .map(PlatformAdministrationResponseMapper::toResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<ValueResponse<Long>> create(@Valid @RequestBody final CreatePlatformOwnerRequest request) {
        final PlatformOwnerId platformOwnerId = platformOwnerAccountPort.create(new CreatePlatformOwnerAccountCommand(
                request.username(), request.password(), request.firstName(), request.lastName(), request.email()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ValueResponse<>(platformOwnerId.value()));
    }

    @PutMapping("/{platformOwnerId}/block")
    public ResponseEntity<Void> block(@PathVariable @Positive final Long platformOwnerId) {
        platformOwnerAccountPort.block(new PlatformOwnerId(platformOwnerId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{platformOwnerId}/unblock")
    public ResponseEntity<Void> unblock(@PathVariable @Positive final Long platformOwnerId) {
        platformOwnerAccountPort.unblock(new PlatformOwnerId(platformOwnerId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{platformOwnerId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable @Positive final Long platformOwnerId,
                                               @Valid @RequestBody final ChangePlatformOwnerPasswordRequest request) {
        platformOwnerAccountPort.changePassword(new PlatformOwnerId(platformOwnerId), request.password());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(PlatformActorRequiredException.class)
    public ResponseEntity<String> handlePlatformActorRequired(final PlatformActorRequiredException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
