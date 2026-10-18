package com.warehouse.pallet.infrastructure.adapter.secondary.document;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.enumeration.PalletHistoryEventType;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;
import com.warehouse.pallet.domain.vo.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "PalletHistoryDocument")
public class PalletHistoryDocument {

    @Id
    private final String id;
    private final PalletId palletId;
    private final PalletHistoryEventType eventType;
    private final PalletStatus palletStatus;
    private final Instant occurredAt;
    private final Long userId;
    private final String username;
    private final DepartmentId departmentId;
    private final String details;

    public PalletHistoryDocument(final String id,
                                 final PalletId palletId,
                                 final PalletHistoryEventType eventType,
                                 final PalletStatus palletStatus,
                                 final Instant occurredAt,
                                 final Long userId,
                                 final String username,
                                 final DepartmentId departmentId,
                                 final String details) {
        this.id = id;
        this.palletId = palletId;
        this.eventType = eventType;
        this.palletStatus = palletStatus;
        this.occurredAt = occurredAt;
        this.userId = userId;
        this.username = username;
        this.departmentId = departmentId;
        this.details = details;
    }

    public static PalletHistoryDocument from(final PalletHistoryEntry entry) {
        return new PalletHistoryDocument(null, entry.palletId(), entry.eventType(), entry.palletStatus(),
                entry.occurredAt(),
                entry.userId() == null ? null : entry.userId().value(),
                entry.userId() == null ? null : entry.userId().username(),
                entry.departmentId(), entry.details());
    }

    public PalletHistoryEntry toEntry() {
        return new PalletHistoryEntry(palletId, eventType, palletStatus, occurredAt,
                userId == null ? null : new UserId(userId, username), departmentId, details);
    }

    public String getId() {
        return id;
    }
}
