package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.enumeration.PalletAuditLogType;

import java.util.List;
import java.util.Objects;

public record PalletAuditLog(PalletId palletId,
                             PalletAuditLogType type,
                             List<FieldDifference> changes) {

    public PalletAuditLog {
        Objects.requireNonNull(palletId, "Pallet ID cannot be null");
        Objects.requireNonNull(type, "Pallet audit log type cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Pallet changes cannot be null"));
    }
}
