package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.palletmanagement.domain.enumeration.PalletHistoryEventType;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;

import java.time.Instant;

public record PalletHistoryEntry(PalletHistoryEventType eventType,
                                 PalletStatus palletStatus,
                                 Instant occurredAt,
                                 UserId userId,
                                 String username,
                                 DepartmentId departmentId,
                                 String details) {
}
