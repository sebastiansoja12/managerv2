package com.warehouse.terminal.domain.model;

import com.warehouse.terminal.domain.enumeration.ExecutionSourceType;

public interface ExecutionSourceResolver {
    ExecutionSourceType getExecutionSourceType();
}
