package com.warehouse.supplier.domain.vo;

import java.time.Instant;

public record DangerousGoodCertification(
        String certificateNumber,
        Instant issueDate,
        Instant expiryDate,
        String authority,
        boolean valid
) {}

