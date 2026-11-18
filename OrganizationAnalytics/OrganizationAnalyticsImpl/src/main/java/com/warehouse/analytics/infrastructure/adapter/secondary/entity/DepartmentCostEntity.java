package com.warehouse.analytics.infrastructure.adapter.secondary.entity;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "analytics.DepartmentCostEntity")
@Table(name = "department_cost")
public class DepartmentCostEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private DepartmentCostId costId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_code", nullable = false, length = 40))
    private DepartmentCode departmentCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private DepartmentCostCategory category;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 3)
    private Currency currency;

    @Column(name = "incurred_on", nullable = false)
    private LocalDate incurredOn;

    @Column(name = "description", length = 500)
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "recorded_by_user_id", nullable = false))
    private UserId recordedBy;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
