package com.warehouse.auth.infrastructure.adapter.primary.consent;

import com.warehouse.auth.AccessAudience;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.port.primary.ImpersonationConsentPort;
import com.warehouse.auth.domain.port.primary.PlatformOwnerAuthenticationPort;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operators/{operatorId}/impersonation-consents")
@AccessUserControl(audience = AccessAudience.PLATFORM_OWNER)
public class PlatformImpersonationConsentController {

    private final ImpersonationConsentPort impersonationConsentPort;

    private final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort;

    public PlatformImpersonationConsentController(final ImpersonationConsentPort impersonationConsentPort,
                                                  final PlatformOwnerAuthenticationPort platformOwnerAuthenticationPort) {
        this.impersonationConsentPort = impersonationConsentPort;
        this.platformOwnerAuthenticationPort = platformOwnerAuthenticationPort;
    }

    @PostMapping
    public ResponseEntity<ImpersonationConsentResponse> request(
            @AuthenticationPrincipal final PlatformOwnerId platformOwnerId,
            @PathVariable final Long operatorId,
            @Valid @RequestBody(required = false) final RequestImpersonationConsentRequest request) {
        final String username = platformOwnerAuthenticationPort.getPlatformOwner(platformOwnerId).getUsername();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ImpersonationConsentResponse.from(impersonationConsentPort.request(platformOwnerId, username,
                        OperatorId.of(operatorId), request != null ? request.reason() : null),
                        impersonationConsentPort.now()));
    }

    @GetMapping("/latest")
    public ResponseEntity<ImpersonationConsentResponse> latest(@AuthenticationPrincipal final PlatformOwnerId platformOwnerId,
                                                               @PathVariable final Long operatorId) {
        return impersonationConsentPort.findLatest(platformOwnerId, OperatorId.of(operatorId))
                .map(consent -> ResponseEntity.ok()
                        .cacheControl(CacheControl.noStore())
                        .body(ImpersonationConsentResponse.from(consent, impersonationConsentPort.now())))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
