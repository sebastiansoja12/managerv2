package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletHandlingHistoryEntryApi(String eventType,
                                            String palletStatus,
                                            Instant occurredAt,
                                            Long userId,
                                            String username,
                                            Long departmentId,
                                            String details) {
}
