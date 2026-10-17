package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorUser;

import java.util.List;

public interface OperatorDirectoryPort {

    List<OperatorUser> findUsers(final OperatorId operatorId);

    UserId createUser(final OperatorId operatorId, final CreateOperatorUserCommand command);

    List<OperatorDepartment> findDepartments(final OperatorId operatorId);

    DepartmentCode createDepartment(final OperatorId operatorId, final CreateOperatorDepartmentCommand command);

    List<OperatorCourier> findCouriers(final OperatorId operatorId);

    SupplierCode createCourier(final OperatorId operatorId, final CreateOperatorCourierCommand command);
}
