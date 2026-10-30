package com.warehouse.auditlog.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AuditLogChangeEmbeddable {

    @Column(name = "field", nullable = false, length = 120)
    private String field;

    @Column(name = "previous_value", length = 1000)
    private String previousValue;

    @Column(name = "current_value", length = 1000)
    private String currentValue;
}
