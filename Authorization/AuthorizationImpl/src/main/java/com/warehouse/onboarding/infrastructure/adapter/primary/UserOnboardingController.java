package com.warehouse.onboarding.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.onboarding.application.port.primary.UserOnboardingPort;
import com.warehouse.onboarding.domain.exception.ConsentVersionMismatchException;
import com.warehouse.onboarding.domain.exception.MissingRequiredConsentException;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.AcceptConsentsApiRequest;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.UserOnboardingApiResponse;
import com.warehouse.onboarding.infrastructure.adapter.primary.dto.UserOnboardingErrorApiResponse;
import com.warehouse.onboarding.infrastructure.adapter.primary.mapper.UserOnboardingApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/me/onboarding")
@AccessUserControl
public class UserOnboardingController {

    private final UserOnboardingPort userOnboardingPort;

    public UserOnboardingController(final UserOnboardingPort userOnboardingPort) {
        this.userOnboardingPort = userOnboardingPort;
    }

    @GetMapping
    public ResponseEntity<UserOnboardingApiResponse> getOnboarding() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(UserOnboardingApiMapper.toResponse(this.userOnboardingPort.getCurrentUserOnboarding()));
    }

    @PostMapping("/consents")
    public ResponseEntity<UserOnboardingApiResponse> acceptConsents(
            @Valid @RequestBody final AcceptConsentsApiRequest consentsRequest) {
        return ResponseEntity.ok(UserOnboardingApiMapper.toResponse(
                this.userOnboardingPort.acceptConsents(UserOnboardingApiMapper.toCommand(consentsRequest))));
    }

    @PostMapping("/tutorial/complete")
    public ResponseEntity<UserOnboardingApiResponse> completeTutorial() {
        return ResponseEntity.ok(UserOnboardingApiMapper.toResponse(this.userOnboardingPort.completeTutorial()));
    }

    @PostMapping("/tutorial/skip")
    public ResponseEntity<UserOnboardingApiResponse> skipTutorial() {
        return ResponseEntity.ok(UserOnboardingApiMapper.toResponse(this.userOnboardingPort.skipTutorial()));
    }

    @PostMapping("/tutorial/restart")
    public ResponseEntity<UserOnboardingApiResponse> restartTutorial() {
        return ResponseEntity.ok(UserOnboardingApiMapper.toResponse(this.userOnboardingPort.restartTutorial()));
    }

    @ExceptionHandler(MissingRequiredConsentException.class)
    public ResponseEntity<UserOnboardingErrorApiResponse> handleMissingConsent(
            final MissingRequiredConsentException exception) {
        return ResponseEntity.badRequest()
                .body(new UserOnboardingErrorApiResponse("MISSING_REQUIRED_CONSENT", exception.getMessage()));
    }

    @ExceptionHandler(ConsentVersionMismatchException.class)
    public ResponseEntity<UserOnboardingErrorApiResponse> handleVersionMismatch(
            final ConsentVersionMismatchException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new UserOnboardingErrorApiResponse("CONSENT_VERSION_CHANGED", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<UserOnboardingErrorApiResponse> handleUnknownConsent(
            final IllegalArgumentException exception) {
        return ResponseEntity.badRequest()
                .body(new UserOnboardingErrorApiResponse("UNKNOWN_CONSENT", exception.getMessage()));
    }
}
