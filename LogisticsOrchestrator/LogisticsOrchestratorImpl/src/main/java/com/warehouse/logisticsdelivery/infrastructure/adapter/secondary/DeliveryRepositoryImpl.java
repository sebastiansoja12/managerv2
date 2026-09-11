package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final OperatorFilteredRepository<DeliveryEntity> repository;
    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    public DeliveryRepositoryImpl(final OperatorFilteredRepository<DeliveryEntity> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Delivery> findById(final DeliveryId deliveryId) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("id.id", deliveryId.getId())
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .eq("type", type)
                .maxResults(1)
                .one()
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Delivery> findActiveByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .eq("type", type)
                .notIn("status", List.of(DeliveryLifecycleStatus.CANCELED, DeliveryLifecycleStatus.COMPLETED))
                .maxResults(1)
                .one()
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findRecent(final int offset, final int limit) {
        return repository.createCriteria(DeliveryEntity.class)
                .desc("created")
                .firstResult(offset)
                .maxResults(limit)
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findByDepartment(final DepartmentId departmentId, final int offset, final int limit) {
        final Criteria<DeliveryEntity> criteria = repository.createCriteria(DeliveryEntity.class);
        final CriteriaBuilder criteriaBuilder = criteria.getCriteriaBuilder();
        final Root<DeliveryEntity> root = criteria.getRoot();
        return criteria
                .and(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("originDepartmentId").get("value"), departmentId.getValue()),
                        criteriaBuilder.equal(root.get("destinationDepartmentId").get("value"), departmentId.getValue())))
                .desc("created")
                .firstResult(offset)
                .maxResults(limit)
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findBySupplier(final SupplierId supplierId, final int offset, final int limit) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("supplierId.value", supplierId.value())
                .desc("created")
                .firstResult(offset)
                .maxResults(limit)
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findAllCreatedBetween(final LocalDateTime createdFromInclusive,
                                                final LocalDateTime createdToExclusive) {
        return repository.createCriteria(DeliveryEntity.class)
                .ge("created", createdFromInclusive)
                .lt("created", createdToExclusive)
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findByTarget(final DeliveryTarget target) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .desc("created")
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findReplacedBy(final DeliveryId replacingDeliveryId) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("replacedByDeliveryId", replacingDeliveryId.getId())
                .desc("created")
                .list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void createOrUpdate(final Delivery delivery) {
        final DeliveryEntity entity = mapper.toEntity(delivery);
        if (deliveryExists(delivery.getDeliveryId())) {
            repository.update(entity);
        } else {
            repository.create(entity);
        }
    }

    private boolean deliveryExists(final DeliveryId deliveryId) {
        return deliveryId != null && repository.createCriteria(DeliveryEntity.class)
                .eq("id.id", deliveryId.getId())
                .one()
                .isPresent();
    }

}
