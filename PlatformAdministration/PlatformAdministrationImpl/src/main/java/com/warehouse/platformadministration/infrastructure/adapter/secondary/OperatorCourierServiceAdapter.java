package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorScope;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper.OperatorDirectoryServiceMapper;

import java.util.List;

public class OperatorCourierServiceAdapter implements OperatorCourierServicePort {

    private final SupplierApiService supplierApiService;

    private final OperatorScopeExecutor operatorScopeExecutor;

    public OperatorCourierServiceAdapter(final SupplierApiService supplierApiService,
                                         final OperatorScopeExecutor operatorScopeExecutor) {
        this.supplierApiService = supplierApiService;
        this.operatorScopeExecutor = operatorScopeExecutor;
    }

    @Override
    public List<OperatorCourier> findAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, () -> supplierApiService.findAll()
                .stream()
                .map(OperatorDirectoryServiceMapper::toOperatorCourier)
                .toList());
    }

    @Override
    public SupplierCode create(final OperatorScope operatorScope, final CreateOperatorCourierCommand command) {
        final SupplierCodeDto supplierCode = operatorScopeExecutor.execute(operatorScope,
                () -> supplierApiService.create(OperatorDirectoryServiceMapper.toCreateSupplierApiRequest(command)));
        return new SupplierCode(supplierCode.value());
    }

    @Override
    public int anonymizeAll(final OperatorScope operatorScope) {
        return operatorScopeExecutor.execute(operatorScope, supplierApiService::anonymizeAll);
    }
}
