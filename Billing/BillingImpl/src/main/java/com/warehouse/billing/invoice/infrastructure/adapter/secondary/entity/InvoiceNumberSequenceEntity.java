package com.warehouse.billing.invoice.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.commonassets.identificator.InvoiceNumberSequenceId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "billing.InvoiceNumberSequenceEntity")
@Table(name = "invoice_number_sequences", uniqueConstraints = @UniqueConstraint(
        name = "uq_invoice_number_sequences_operator_type_period",
        columnNames = {"operator_id", "invoice_type", "period"}))
public class InvoiceNumberSequenceEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private InvoiceNumberSequenceId sequenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false, length = 20)
    private InvoiceType type;

    @Column(name = "period", nullable = false, length = 7)
    private String period;

    @Column(name = "last_value", nullable = false)
    private long lastValue;

    public InvoiceNumberSequenceEntity(final InvoiceNumberSequenceId sequenceId, final InvoiceType type,
                                       final String period) {
        this.sequenceId = sequenceId;
        this.type = type;
        this.period = period;
        this.lastValue = 0L;
    }

    public long increment() {
        this.lastValue = this.lastValue + 1;
        return this.lastValue;
    }
}
