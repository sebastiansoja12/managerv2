package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

import java.util.List;
import java.util.Optional;

public class OperatorRepositoryImpl implements OperatorRepository {

    private final OperatorReadRepository repository;

    public OperatorRepositoryImpl(final OperatorReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Operator> findAll() {
        return repository.findAll()
                .stream()
                .map(OperatorMapper::toModel)
                .toList();
    }

    @Override
    public Optional<Operator> findById(final OperatorId operatorId) {
        return repository.findById(operatorId)
                .map(OperatorMapper::toModel);
    }

    @Override
    public Optional<Long> findMaxOperatorIdValue() {
        return repository.findMaxOperatorIdValue();
    }

    @Override
    public boolean existsById(final OperatorId operatorId) {
        return repository.existsById(operatorId);
    }

    @Override
    public void save(final Operator operator) {
        repository.save(OperatorMapper.toEntity(operator));
    }
}
