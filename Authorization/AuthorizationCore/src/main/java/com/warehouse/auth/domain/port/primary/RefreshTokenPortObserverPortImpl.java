package com.warehouse.auth.domain.port.primary;


import com.warehouse.auth.domain.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Slf4j
public class RefreshTokenPortObserverPortImpl implements RefreshTokenPortObserverPort {

    private final RefreshTokenService refreshTokenService;

    public RefreshTokenPortObserverPortImpl(final RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    @Scheduled(cron = "${refreshtoken.delete}")
    public void deleteExpiredRefreshTokens() {
        log.info("Deleting expired refresh tokens...");
        refreshTokenService.delete(LocalDateTime.now());
        log.info("Expired refresh tokens successfully deleted");
    }
}
