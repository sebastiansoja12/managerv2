package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryHistoryRepository;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryHistoryEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryHistoryEntityMapper;

import java.util.List;

public class DeliveryHistoryRepositoryImpl implements DeliveryHistoryRepository {

    private final OperatorFilteredRepository<DeliveryHistoryEntity> repository;
    private final DeliveryHistoryEntityMapper mapper;

    public DeliveryHistoryRepositoryImpl(final OperatorFilteredRepository<DeliveryHistoryEntity> repository,
                                         final DeliveryHistoryEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(final DeliveryHistoryEntry entry) {
        this.repository.create(this.mapper.toEntity(entry));
    }

    @Override
    public List<DeliveryHistoryEntry> findByDeliveryId(final DeliveryId deliveryId) {
        return this.repository.createCriteria(DeliveryHistoryEntity.class)
                .eq("deliveryId", deliveryId.getId())
                .desc("occurredAt")
                .list().stream()
                .map(this.mapper::toEntry)
                .toList();
    }
}
