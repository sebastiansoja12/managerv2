package com.warehouse.organisationstructure.operator.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;

import java.util.List;

public interface OperatorPort {
    List<Operator> getAll();
    Operator getById(final OperatorId operatorId);
    boolean exists(final OperatorId operatorId);
    boolean isActive(final OperatorId operatorId);
    OperatorId create(final CreateOperatorCommand command);
    Operator update(final OperatorId operatorId, final UpdateOperatorCommand command);
}
