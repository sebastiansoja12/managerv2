package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper.OperatorDirectoryServiceMapper;

import java.util.List;

public class OperatorDepartmentServiceAdapter implements OperatorDepartmentServicePort {

    private final DepartmentApiService departmentApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorDepartmentServiceAdapter(final DepartmentApiService departmentApiService,
                                            final OperatorScopeExecutor operatorScopeExecutor) {
        this.departmentApiService = departmentApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public List<OperatorDepartment> findAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, () -> departmentApiService.getDepartmentDetails()
                .stream()
                .map(OperatorDirectoryServiceMapper::toOperatorDepartment)
                .toList());
    }

    @Override
    public DepartmentCode create(final OperatorScope operatorScope, final CreateOperatorDepartmentCommand command) {
        final DepartmentCodeDto departmentCode = operatorScopeExecutor.execute(operatorScope,
                () -> departmentApiService.createDepartment(
                        OperatorDirectoryServiceMapper.toCreateDepartmentApiRequest(command)));
        return new DepartmentCode(departmentCode.value());
    }
}
