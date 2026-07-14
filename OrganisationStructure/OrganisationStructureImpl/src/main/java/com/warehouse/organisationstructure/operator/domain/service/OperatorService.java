package com.warehouse.organisationstructure.operator.domain.service;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;

import java.util.List;

public interface OperatorService {
    List<Operator> getAll();
    Operator getById(OperatorId operatorId);
    boolean exists(OperatorId operatorId);
    boolean isActive(OperatorId operatorId);
    void create(final Operator operator);
    Operator update(final OperatorId operatorId, final UpdateOperatorCommand command);
    OperatorId nextOperatorId();

    void assignRegisteringUser(final OperatorId operatorId, final UserId user);
}
