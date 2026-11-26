package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;

import java.util.List;

public interface DepartmentAssignmentRunServicePort {

    List<DepartmentAssignmentRunView> findLatest(final OperatorId operatorId, final int limit);
}
