package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;

public interface OperatorJobServicePort {

    OperatorJobType jobType();

    OperatorJobRun run(final OperatorId operatorId);
}
