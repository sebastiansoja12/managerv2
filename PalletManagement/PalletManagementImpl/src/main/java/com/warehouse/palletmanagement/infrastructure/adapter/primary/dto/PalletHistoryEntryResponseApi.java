package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record PalletHistoryEntryResponseApi(String eventType,
                                            String palletStatus,
                                            Instant occurredAt,
                                            Long userId,
                                            String username,
                                            Long departmentId,
                                            String details) {
}
