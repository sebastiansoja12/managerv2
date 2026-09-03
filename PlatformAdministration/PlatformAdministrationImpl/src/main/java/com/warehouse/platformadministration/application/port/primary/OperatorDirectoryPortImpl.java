package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OperatorDirectoryPortImpl implements OperatorDirectoryPort {

    private final OperatorServicePort operatorServicePort;

    private final OperatorUserServicePort operatorUserServicePort;

    private final OperatorDepartmentServicePort operatorDepartmentServicePort;

    private final OperatorCourierServicePort operatorCourierServicePort;

    public OperatorDirectoryPortImpl(final OperatorServicePort operatorServicePort,
                                     final OperatorUserServicePort operatorUserServicePort,
                                     final OperatorDepartmentServicePort operatorDepartmentServicePort,
                                     final OperatorCourierServicePort operatorCourierServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorUserServicePort = operatorUserServicePort;
        this.operatorDepartmentServicePort = operatorDepartmentServicePort;
        this.operatorCourierServicePort = operatorCourierServicePort;
    }

    @Override
    public List<OperatorUser> findUsers(final OperatorId operatorId) {
        return operatorUserServicePort.findAll(scope(operatorId));
    }

    @Override
    public UserId createUser(final OperatorId operatorId, final CreateOperatorUserCommand command) {
        final UserId userId = operatorUserServicePort.create(scope(operatorId), command);
        log.info("Platform owner created user {} for operator {}", userId.value(), operatorId.value());
        return userId;
    }

    @Override
    public List<OperatorDepartment> findDepartments(final OperatorId operatorId) {
        return operatorDepartmentServicePort.findAll(scope(operatorId));
    }

    @Override
    public DepartmentCode createDepartment(final OperatorId operatorId, final CreateOperatorDepartmentCommand command) {
        final DepartmentCode departmentCode = operatorDepartmentServicePort.create(scope(operatorId), command);
        log.info("Platform owner created department {} for operator {}", departmentCode.getValue(), operatorId.value());
        return departmentCode;
    }

    @Override
    public List<OperatorCourier> findCouriers(final OperatorId operatorId) {
        return operatorCourierServicePort.findAll(scope(operatorId));
    }

    @Override
    public SupplierCode createCourier(final OperatorId operatorId, final CreateOperatorCourierCommand command) {
        final SupplierCode supplierCode = operatorCourierServicePort.create(scope(operatorId), command);
        log.info("Platform owner created courier for operator {}", operatorId.value());
        return supplierCode;
    }

    private OperatorScope scope(final OperatorId operatorId) {
        return operatorServicePort.findScope(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
    }
}
