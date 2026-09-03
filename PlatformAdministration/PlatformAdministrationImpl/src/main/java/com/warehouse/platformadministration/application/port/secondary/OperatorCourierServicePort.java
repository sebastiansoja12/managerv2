package com.warehouse.platformadministration.application.port.secondary;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorScope;

import java.util.List;

public interface OperatorCourierServicePort {

    List<OperatorCourier> findAll(final OperatorScope operatorScope);

    SupplierCode create(final OperatorScope operatorScope, final CreateOperatorCourierCommand command);

    int anonymizeAll(final OperatorScope operatorScope);
}
