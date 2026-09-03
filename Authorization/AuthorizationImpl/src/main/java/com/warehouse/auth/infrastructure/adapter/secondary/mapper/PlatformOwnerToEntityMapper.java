package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.PlatformOwnerEntity;

public abstract class PlatformOwnerToEntityMapper {

    public static PlatformOwnerEntity map(final PlatformOwner platformOwner) {
        return PlatformOwnerEntity.builder()
                .platformOwnerId(platformOwner.getPlatformOwnerId())
                .username(platformOwner.getUsername())
                .password(platformOwner.getPassword())
                .firstName(platformOwner.getFirstName())
                .lastName(platformOwner.getLastName())
                .email(platformOwner.getEmail())
                .deleted(platformOwner.isDeleted())
                .createdAt(platformOwner.createdAt())
                .updatedAt(platformOwner.updatedAt())
                .build();
    }
}
