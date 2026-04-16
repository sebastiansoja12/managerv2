package com.warehouse.auth.domain.port.primary;

public interface RefreshTokenPortObserverPort {
    void deleteExpiredRefreshTokens();
}
