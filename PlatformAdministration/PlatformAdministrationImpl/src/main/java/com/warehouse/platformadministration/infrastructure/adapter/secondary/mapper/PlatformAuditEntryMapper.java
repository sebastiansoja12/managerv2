package com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.PlatformAuditEntryEntity;

public abstract class PlatformAuditEntryMapper {

    public static PlatformAuditEntryEntity toEntity(final PlatformAuditEntry entry) {
        return PlatformAuditEntryEntity.builder()
                .id(entry.id())
                .platformOwnerId(entry.platformOwnerId().value())
                .platformOwnerUsername(entry.platformOwnerUsername())
                .operatorId(entry.operatorId() != null ? entry.operatorId().value() : null)
                .impersonatedUserId(entry.impersonatedUserId() != null ? entry.impersonatedUserId().value() : null)
                .impersonation(entry.impersonation())
                .httpMethod(entry.httpMethod())
                .path(entry.path())
                .statusCode(entry.statusCode())
                .success(entry.success())
                .createdAt(entry.createdAt())
                .build();
    }

    public static PlatformAuditEntry toModel(final PlatformAuditEntryEntity entity) {
        return new PlatformAuditEntry(
                entity.getId(),
                new PlatformOwnerId(entity.getPlatformOwnerId()),
                entity.getPlatformOwnerUsername(),
                entity.getOperatorId() != null ? OperatorId.of(entity.getOperatorId()) : null,
                entity.getImpersonatedUserId() != null ? new UserId(entity.getImpersonatedUserId()) : null,
                entity.isImpersonation(),
                entity.getHttpMethod(),
                entity.getPath(),
                entity.getStatusCode(),
                entity.isSuccess(),
                entity.getCreatedAt());
    }
}
