package com.warehouse.pallet.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record PalletHistoryEntryApi(String eventType,
                                    String palletStatus,
                                    Instant occurredAt,
                                    Long userId,
                                    String username,
                                    Long departmentId,
                                    String details) {
}
