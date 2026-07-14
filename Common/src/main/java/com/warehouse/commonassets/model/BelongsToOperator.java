package com.warehouse.commonassets.model;

import org.hibernate.envers.Audited;

import com.warehouse.commonassets.identificator.OperatorId;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@Audited
public abstract class BelongsToOperator implements OperatorContext {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operator_id"))
    private OperatorId operatorId;

    @Override
    public OperatorId operatorId() {
        return operatorId;
    }

    @Override
    public void assignOperator(final OperatorId operatorId) {
        this.operatorId = operatorId;
    }
}
