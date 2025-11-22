package com.warehouse.commonassets.repository.baseentity;

import com.warehouse.commonassets.model.BelongsToDepartment;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<T> extends BelongsToDepartment {
    public abstract T getId();
}
