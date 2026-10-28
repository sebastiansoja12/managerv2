package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.shipment.application.port.secondary.CurrentDepartmentCodePort;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;

import java.util.Optional;

public class OperatorContextDepartmentCodeAdapter implements CurrentDepartmentCodePort {

    private final OperatorContextProvider operatorContextProvider;
    private final DepartmentServicePort departmentServicePort;

    public OperatorContextDepartmentCodeAdapter(final OperatorContextProvider operatorContextProvider,
                                                final DepartmentServicePort departmentServicePort) {
        this.operatorContextProvider = operatorContextProvider;
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public Optional<DepartmentCode> currentDepartmentCode() {
        // external requests run without a department in context
        return operatorContextProvider.currentContext()
                .map(OperatorDetails::departmentId)
                .map(departmentServicePort::getDepartmentCode);
    }
}
