package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.enumeration.PalletHistoryEventType;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.vo.UserContext;
import com.warehouse.pallet.domain.vo.UserId;

import java.time.Instant;

public record PalletHistoryEntry(PalletId palletId,
                                 PalletHistoryEventType eventType,
                                 PalletStatus palletStatus,
                                 Instant occurredAt,
                                 UserId userId,
                                 DepartmentId departmentId,
                                 String details) {

    public static PalletHistoryEntry of(final Pallet pallet,
                                        final PalletHistoryEventType eventType,
                                        final UserContext userContext,
                                        final String details) {
        return new PalletHistoryEntry(
                pallet.getPalletId(),
                eventType,
                pallet.getPalletStatus(),
                Instant.now(),
                userContext == null ? null : userContext.userId(),
                userContext == null ? null : userContext.departmentId(),
                details);
    }
}
