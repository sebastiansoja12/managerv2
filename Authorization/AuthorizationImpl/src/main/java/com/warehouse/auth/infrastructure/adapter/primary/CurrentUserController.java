package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.domain.model.FullNameChangeCommand;
import com.warehouse.auth.infrastructure.dto.*;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;

@RestController
@RequestMapping("/auth/me")
@AccessUserControl
public class CurrentUserController {

    private final CurrentUserAuthenticationPort currentUserAuthenticationPort;

    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    private final ResponseMapper responseMapper;

    public CurrentUserController(final CurrentUserAuthenticationPort currentUserAuthenticationPort,
                                 final UserPort userPort,
                                 final PasswordEncoder passwordEncoder,
                                 final ResponseMapper responseMapper) {
        this.currentUserAuthenticationPort = currentUserAuthenticationPort;
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(map(user));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody final ChangePasswordRequestDto request) {
        if (request == null || isBlank(request.currentPassword()) || isBlank(request.newPassword())) {
            return ResponseEntity.badRequest().body("Current password and new password are required");
        }

        final User user = currentUserAuthenticationPort.getCurrentUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect");
        }

        userPort.changePassword(user.getUserId(), passwordEncoder.encode(request.newPassword()));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/fullnames")
    public ResponseEntity<?> changeFullName(@RequestBody final FullNameRequestApiDto request) {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        final FullNameChangeCommand fullNameChangeCommand = new FullNameChangeCommand(request.firstName(),
                request.lastName(), user.getUserId());
        userPort.changeFullName(fullNameChangeCommand);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/language")
    public ResponseEntity<?> changeLanguage(@RequestBody final ChangeLanguageRequestDto request) {
        if (request == null || isBlank(request.language()) || !isSupportedLanguage(request.language())) {
            return ResponseEntity.badRequest().body("Supported language is required");
        }

        final User user = currentUserAuthenticationPort.getCurrentUser();
        userPort.changeLanguage(user.getUserId(), request.language());
        final User changedUser = userPort.findUser(user.getUserId());
        return ResponseEntity.ok(map(changedUser));
    }

    @PostMapping("/api-key")
    public ResponseEntity<GeneratedApiKeyResponseDto> regenerateApiKey() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        final String apiKey = userPort.regenerateApiKey(user.getUserId());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(new GeneratedApiKeyResponseDto(apiKey));
    }

    @DeleteMapping("/api-key")
    public ResponseEntity<Void> deleteApiKey() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        userPort.deleteApiKey(user.getUserId());
        return ResponseEntity.noContent().build();
    }

    private CurrentUserProfileDto map(final User user) {
        return responseMapper.mapCurrentUser(user, userPort.getDepartmentCode(user.getDepartmentId()));
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    private boolean isSupportedLanguage(final String language) {
        return "pl".equals(language) || "en".equals(language) || "de".equals(language);
    }
}
