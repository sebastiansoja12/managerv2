package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.port.secondary.CurrentUserServicePort;

public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    private final CurrentUserApiService currentUserApiService;

    public CurrentUserServiceAdapter(final CurrentUserApiService currentUserApiService) {
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserApiService.getCurrentUserId();
    }
}
