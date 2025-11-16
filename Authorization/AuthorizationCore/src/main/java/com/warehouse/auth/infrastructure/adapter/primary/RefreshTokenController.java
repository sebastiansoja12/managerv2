package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.auth.domain.service.RefreshTokenService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth/refresh-tokens")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenController(final RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN_READ')")
    public ResponseEntity<Void> fireRefreshTokenClear() {
        refreshTokenService.delete(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }
}
