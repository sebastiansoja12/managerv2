package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import java.time.Instant;

public record OperatorDataErasureResponse(Long operatorId,
                                          String requestedBy,
                                          Instant executedAt,
                                          int usersAnonymized,
                                          int couriersAnonymized,
                                          int devicesRetired) {
}
