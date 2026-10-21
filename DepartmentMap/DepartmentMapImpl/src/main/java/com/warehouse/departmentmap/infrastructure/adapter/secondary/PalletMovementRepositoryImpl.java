package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.departmentmap.application.port.secondary.PalletMovementRepository;
import com.warehouse.departmentmap.domain.model.PalletMovement;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.entity.PalletMovementEntity;
import com.warehouse.departmentmap.infrastructure.adapter.secondary.mapper.PalletMovementEntityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PalletMovementRepositoryImpl implements PalletMovementRepository {

    private static final int HISTORY_LIMIT = 200;

    private final OperatorFilteredRepository<PalletMovementEntity> repository;

    public PalletMovementRepositoryImpl(final OperatorFilteredRepository<PalletMovementEntity> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(final PalletMovement palletMovement) {
        this.repository.create(PalletMovementEntityMapper.toEntity(palletMovement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PalletMovement> findHistory(final PalletTrackingNumber palletTrackingNumber) {
        return this.repository.createCriteria(PalletMovementEntity.class)
                .eq("palletTrackingNumber.value", palletTrackingNumber.value())
                .desc("occurredAt")
                .maxResults(HISTORY_LIMIT)
                .list()
                .stream()
                .map(PalletMovementEntityMapper::toModel)
                .toList();
    }
}
