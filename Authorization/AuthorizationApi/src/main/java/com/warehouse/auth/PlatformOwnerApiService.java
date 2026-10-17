package com.warehouse.auth;

import com.warehouse.auth.infrastructure.dto.CreatePlatformOwnerApiRequest;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerDto;
import com.warehouse.auth.infrastructure.dto.PlatformOwnerIdDto;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;

public interface PlatformOwnerApiService {
    List<PlatformOwnerDto> findAll();
    PlatformOwnerIdDto create(final CreatePlatformOwnerApiRequest request);
    void block(final PlatformOwnerId platformOwnerId, final PlatformOwnerId actingPlatformOwnerId);
    void unblock(final PlatformOwnerId platformOwnerId);
    void changePassword(final PlatformOwnerId platformOwnerId, final String password);
}
