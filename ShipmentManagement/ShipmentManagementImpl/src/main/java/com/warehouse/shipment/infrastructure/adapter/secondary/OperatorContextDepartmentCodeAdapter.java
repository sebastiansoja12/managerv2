package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.secondary.CurrentDepartmentCodePort;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;

public class OperatorContextDepartmentCodeAdapter implements CurrentDepartmentCodePort {

    private final OperatorContextProvider operatorContextProvider;
    private final DepartmentServicePort departmentServicePort;

    public OperatorContextDepartmentCodeAdapter(final OperatorContextProvider operatorContextProvider,
                                                final DepartmentServicePort departmentServicePort) {
        this.operatorContextProvider = operatorContextProvider;
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public DepartmentCode currentDepartmentCode() {
        final DepartmentId departmentId = operatorContextProvider.currentDepartmentId()
                .orElseThrow(() -> new IllegalStateException(
                        "Department context is required for department based tracking number"));
        return departmentServicePort.getDepartmentCode(departmentId);
    }
}
