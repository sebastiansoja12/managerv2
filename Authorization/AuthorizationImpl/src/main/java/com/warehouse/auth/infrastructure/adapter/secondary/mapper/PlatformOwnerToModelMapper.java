package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.PlatformOwnerEntity;

public abstract class PlatformOwnerToModelMapper {

    public static PlatformOwner map(final PlatformOwnerEntity entity) {
        return new PlatformOwner(
                entity.getPlatformOwnerId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
