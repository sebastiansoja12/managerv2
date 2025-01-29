package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.domain.enumeration.ExecutionSourceType;

public interface ExecutionSourceResolver {
    ExecutionSourceType getExecutionSourceType();
}
