package com.warehouse.auth.domain.vo;

import com.warehouse.commonassets.identificator.PlatformOwnerId;

public record Impersonation(PlatformOwnerId platformOwnerId, String platformOwnerUsername, Long consentId) {

    public Impersonation(final PlatformOwnerId platformOwnerId, final String platformOwnerUsername) {
        this(platformOwnerId, platformOwnerUsername, null);
    }
}
