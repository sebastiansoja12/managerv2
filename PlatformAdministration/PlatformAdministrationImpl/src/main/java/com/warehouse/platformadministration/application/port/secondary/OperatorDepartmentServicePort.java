package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.List;

public interface OperatorDepartmentServicePort {

    List<OperatorDepartment> findAll(final OperatorScope operatorScope);

    DepartmentCode create(final OperatorScope operatorScope, final CreateOperatorDepartmentCommand command);
}
