package com.warehouse.auth.infrastructure.adapter.primary.consent;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.exception.ImpersonationConsentNotFoundException;
import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.primary.ImpersonationConsentPort;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.secondary.filter.JwtAuthenticationFilter;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.OperatorId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/impersonation-consents")
@AccessUserControl(minimumLevel = AccountLevel.SUPERADMIN)
public class ImpersonationConsentController {

    private final ImpersonationConsentPort impersonationConsentPort;

    private final AuthenticationService authenticationService;

    private final UserService userService;

    public ImpersonationConsentController(final ImpersonationConsentPort impersonationConsentPort,
                                          final AuthenticationService authenticationService,
                                          final UserService userService) {
        this.impersonationConsentPort = impersonationConsentPort;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ImpersonationConsentResponse>> pending() {
        return list(impersonationConsentPort.findPending(authenticationService.currentOperatorId()));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ImpersonationConsentResponse>> active() {
        return list(impersonationConsentPort.findActive(authenticationService.currentOperatorId()));
    }

    @PostMapping("/{consentId}/approve")
    public ResponseEntity<ImpersonationConsentResponse> approve(@PathVariable final Long consentId,
                                                                @Valid @RequestBody final ApproveImpersonationConsentRequest request,
                                                                final HttpServletRequest servletRequest) {
        requireOwnSession(servletRequest);
        final OrganizationUser user = currentUser();
        return respond(impersonationConsentPort.approve(consentId, operatorId(), user.getUserId(), user.getUsername(),
                request.durationMinutes()));
    }

    @PostMapping("/{consentId}/reject")
    public ResponseEntity<ImpersonationConsentResponse> reject(@PathVariable final Long consentId,
                                                               final HttpServletRequest servletRequest) {
        requireOwnSession(servletRequest);
        final OrganizationUser user = currentUser();
        return respond(impersonationConsentPort.reject(consentId, operatorId(), user.getUserId(), user.getUsername()));
    }

    @PostMapping("/{consentId}/revoke")
    public ResponseEntity<ImpersonationConsentResponse> revoke(@PathVariable final Long consentId,
                                                               final HttpServletRequest servletRequest) {
        requireOwnSession(servletRequest);
        final OrganizationUser user = currentUser();
        return respond(impersonationConsentPort.revoke(consentId, operatorId(), user.getUserId(), user.getUsername()));
    }

    @ExceptionHandler(ImpersonationConsentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(final ImpersonationConsentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(final IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(final IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(ImpersonationDecisionForbiddenException.class)
    public ResponseEntity<String> handleForbidden(final ImpersonationDecisionForbiddenException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    private void requireOwnSession(final HttpServletRequest servletRequest) {
        if (servletRequest.getAttribute(JwtAuthenticationFilter.IMPERSONATION_ATTRIBUTE) != null) {
            throw new ImpersonationDecisionForbiddenException();
        }
    }

    private OrganizationUser currentUser() {
        return userService.findUserById(authenticationService.currentUserId());
    }

    private OperatorId operatorId() {
        return authenticationService.currentOperatorId();
    }

    private ResponseEntity<List<ImpersonationConsentResponse>> list(final List<ImpersonationConsent> consents) {
        final Instant now = impersonationConsentPort.now();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(consents.stream().map(consent -> ImpersonationConsentResponse.from(consent, now)).toList());
    }

    private ResponseEntity<ImpersonationConsentResponse> respond(final ImpersonationConsent consent) {
        return ResponseEntity.ok(ImpersonationConsentResponse.from(consent, impersonationConsentPort.now()));
    }

    static final class ImpersonationDecisionForbiddenException extends RuntimeException {

        ImpersonationDecisionForbiddenException() {
            super("Impersonation consent cannot be decided during impersonation");
        }
    }
}
