package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.auth.UserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.port.secondary.UserServicePort;

public class UserServiceClient implements UserServicePort {

    private final UserApiService userApiService;

    public UserServiceClient(final UserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @Override
    public String getUsername(final UserId userId) {
        final UserDto user = this.userApiService.findById(userId);
        return user.username();
    }
}
