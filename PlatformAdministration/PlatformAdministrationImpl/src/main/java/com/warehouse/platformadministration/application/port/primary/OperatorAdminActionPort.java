package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;

import java.util.List;

public interface OperatorAdminActionPort {

    List<OperatorJobType> jobTypes();

    OperatorJobRun runJob(final OperatorId operatorId, final OperatorJobType jobType);

    List<DepartmentAssignmentRunView> findCourierAssignmentRuns(final OperatorId operatorId);
}
