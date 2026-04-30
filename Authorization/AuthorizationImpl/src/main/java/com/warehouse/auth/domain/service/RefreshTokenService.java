package com.warehouse.auth.domain.service;

import java.time.LocalDateTime;

public interface RefreshTokenService {
    void delete(LocalDateTime now);
}
