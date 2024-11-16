package com.warehouse.deliveryreject.domain.model;

import com.warehouse.deliveryreject.domain.enumeration.ExecutionSourceType;

public interface ExecutionSourceResolver {
    ExecutionSourceType getExecutionSourceType();
}
