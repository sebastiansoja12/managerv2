package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.ChangeLanguageRequestDto;
import com.warehouse.auth.infrastructure.dto.ChangePasswordRequestDto;

@RestController
@RequestMapping("/auth/me")
@AccessUserControl
public class CurrentUserController {

    private final CurrentUserAuthenticationPort currentUserAuthenticationPort;

    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    public CurrentUserController(final CurrentUserAuthenticationPort currentUserAuthenticationPort,
                                 final UserPort userPort,
                                 final PasswordEncoder passwordEncoder) {
        this.currentUserAuthenticationPort = currentUserAuthenticationPort;
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        final User user = currentUserAuthenticationPort.getCurrentUser();
        return ResponseEntity.ok(ResponseMapper.map(user));
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

    @PutMapping("/language")
    public ResponseEntity<?> changeLanguage(@RequestBody final ChangeLanguageRequestDto request) {
        if (request == null || isBlank(request.language()) || !isSupportedLanguage(request.language())) {
            return ResponseEntity.badRequest().body("Supported language is required");
        }

        final User user = currentUserAuthenticationPort.getCurrentUser();
        userPort.changeLanguage(user.getUserId(), request.language());
        final User changedUser = userPort.findUser(user.getUserId());
        return ResponseEntity.ok(ResponseMapper.map(changedUser));
    }

    private boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    private boolean isSupportedLanguage(final String language) {
        return "pl".equals(language) || "en".equals(language) || "de".equals(language);
    }
}
