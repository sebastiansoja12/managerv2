package com.warehouse.auth.domain.port.primary;


import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
public class RefreshTokenPortObserverPortImpl implements RefreshTokenPortObserverPort {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Scheduled(cron = "${refreshtoken.delete}")
    public void deleteExpiredRefreshTokens() {
        log.info("Deleting expired refresh tokens...");
        refreshTokenRepository.delete(LocalDateTime.now());
        log.info("Expired refresh tokens successfully deleted");
    }
}
