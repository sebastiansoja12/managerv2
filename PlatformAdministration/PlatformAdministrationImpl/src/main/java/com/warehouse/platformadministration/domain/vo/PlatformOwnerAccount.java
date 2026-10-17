package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.time.Instant;

public record PlatformOwnerAccount(PlatformOwnerId platformOwnerId,
                                   String username,
                                   String firstName,
                                   String lastName,
                                   String email,
                                   boolean active,
                                   Instant createdAt) {
}
