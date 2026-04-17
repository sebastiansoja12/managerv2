package com.warehouse.organisationstructure.operator.domain.port.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.domain.model.Operator;

import java.util.List;
import java.util.Optional;

public interface OperatorRepository {
    List<Operator> findAll();
    Optional<Operator> findById(final OperatorId operatorId);
    Optional<Long> findMaxOperatorIdValue();
    boolean existsById(final OperatorId operatorId);
    void save(final Operator operator);
}
