package com.warehouse.supplier.infrastructure.adapter.primary.dto;

import java.time.Instant;

public record DangerousGoodCertificationApi(
        String certificateNumber,
        Instant issueDate,
        Instant expiryDate,
        String authority,
        boolean valid
) {
}
