package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.envers.Audited;

import java.time.Instant;

@Embeddable
@Audited
public record DangerousGoodCertification(
        @Column(name = "certificate_number") String certificateNumber,
        @Column(name = "issue_date") Instant issueDate,
        @Column(name = "expiry_date") Instant expiryDate,
        @Column(name = "authority") String authority,
        @Column(name = "valid") boolean valid
) {
    public DangerousGoodCertification() {
        this(null, null, null, null, false);
    }
}

