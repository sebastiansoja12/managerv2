package com.warehouse.platformadministration.domain.vo;

import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;

public record OperatorJobRun(OperatorJobType jobType, boolean asynchronous, int affected, Integer total) {
}
