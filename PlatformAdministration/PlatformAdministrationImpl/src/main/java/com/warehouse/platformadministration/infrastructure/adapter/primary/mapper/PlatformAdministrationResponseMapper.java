package com.warehouse.platformadministration.infrastructure.adapter.primary.mapper;

import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;
import com.warehouse.platformadministration.domain.vo.PlatformOwnerAccount;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PlatformAuditEntryResponse;
import com.warehouse.platformadministration.infrastructure.adapter.primary.api.PlatformOwnerAccountResponse;

public abstract class PlatformAdministrationResponseMapper {

    public static PlatformAuditEntryResponse toResponse(final PlatformAuditEntry entry) {
        return new PlatformAuditEntryResponse(
                entry.id(),
                entry.platformOwnerId().value(),
                entry.platformOwnerUsername(),
                entry.operatorId() != null ? entry.operatorId().value() : null,
                entry.impersonatedUserId() != null ? entry.impersonatedUserId().value() : null,
                entry.impersonation(),
                entry.httpMethod(),
                entry.path(),
                entry.statusCode(),
                entry.success(),
                entry.createdAt());
    }

    public static PlatformOwnerAccountResponse toResponse(final PlatformOwnerAccount account) {
        return new PlatformOwnerAccountResponse(
                account.platformOwnerId().value(),
                account.username(),
                account.firstName(),
                account.lastName(),
                account.email(),
                account.active(),
                account.createdAt());
    }
}
