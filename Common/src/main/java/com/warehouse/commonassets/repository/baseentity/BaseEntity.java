package com.warehouse.commonassets.repository.baseentity;

import com.warehouse.commonassets.model.BelongsToOperator;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<T> extends BelongsToOperator {
    public abstract T getId();
}
