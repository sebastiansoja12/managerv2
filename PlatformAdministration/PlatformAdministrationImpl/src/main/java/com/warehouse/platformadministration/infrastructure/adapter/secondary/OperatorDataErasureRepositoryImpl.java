package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.OperatorDataErasureRepository;
import com.warehouse.platformadministration.domain.model.OperatorDataErasure;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.OperatorDataErasureEntity;

import java.util.Optional;

public class OperatorDataErasureRepositoryImpl implements OperatorDataErasureRepository {

    private final OperatorDataErasureReadRepository repository;

    public OperatorDataErasureRepositoryImpl(final OperatorDataErasureReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public OperatorDataErasure save(final OperatorDataErasure erasure) {
        return toModel(repository.save(new OperatorDataErasureEntity(erasure.id(), erasure.operatorId().value(),
                erasure.requestedBy(), erasure.executedAt(), erasure.usersAnonymized(), erasure.couriersAnonymized(),
                erasure.devicesRetired())));
    }

    @Override
    public Optional<OperatorDataErasure> findLatest(final OperatorId operatorId) {
        return repository.findFirstByOperatorIdOrderByExecutedAtDesc(operatorId.value()).map(this::toModel);
    }

    private OperatorDataErasure toModel(final OperatorDataErasureEntity entity) {
        return new OperatorDataErasure(entity.getId(), OperatorId.of(entity.getOperatorId()), entity.getRequestedBy(),
                entity.getExecutedAt(), entity.getUsersAnonymized(), entity.getCouriersAnonymized(),
                entity.getDevicesRetired());
    }
}
