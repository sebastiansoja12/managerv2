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
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierDelivery;
import com.warehouse.logisticsdelivery.application.port.secondary.AwaitingCourierQuery;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryMethod;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryTargetType;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
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
    public Optional<Delivery> findCurrentByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .eq("type", type)
                .ne("status", DeliveryLifecycleStatus.CANCELED)
                .desc("created")
                .maxResults(1)
                .one()
                .or(() -> repository.createCriteria(DeliveryEntity.class)
                        .eq("targetType", target.type())
                        .eq("targetId", target.id())
                        .eq("type", type)
                        .desc("created")
                        .maxResults(1)
                        .one())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Delivery> findActiveByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("targetType", target.type())
                .eq("targetId", target.id())
                .eq("type", type)
                .notIn("status", List.of(DeliveryLifecycleStatus.CANCELED, DeliveryLifecycleStatus.COMPLETED))
                .desc("created")
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
    public long countCreatedForDestinationBetween(final DepartmentId departmentId,
                                                  final LocalDateTime createdFromInclusive,
                                                  final LocalDateTime createdToExclusive) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("destinationDepartmentId.value", departmentId.getValue())
                .ge("created", createdFromInclusive)
                .lt("created", createdToExclusive)
                .ne("status", DeliveryLifecycleStatus.CANCELED)
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveByDepartment(final DepartmentId departmentId) {
        return activeByDepartment(departmentId).count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> findActiveByDepartment(final DepartmentId departmentId) {
        return activeByDepartment(departmentId).list().stream()
                .map(mapper::toDomain)
                .toList();
    }

    private Criteria<DeliveryEntity> activeByDepartment(final DepartmentId departmentId) {
        final Criteria<DeliveryEntity> criteria = repository.createCriteria(DeliveryEntity.class);
        final CriteriaBuilder criteriaBuilder = criteria.getCriteriaBuilder();
        final Root<DeliveryEntity> root = criteria.getRoot();
        return criteria
                .and(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("originDepartmentId").get("value"), departmentId.getValue()),
                        criteriaBuilder.equal(root.get("destinationDepartmentId").get("value"), departmentId.getValue())))
                .notIn("status", List.of(DeliveryLifecycleStatus.CANCELED, DeliveryLifecycleStatus.COMPLETED));
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
    public List<AwaitingCourierDelivery> findAwaitingCourier(final AwaitingCourierQuery query) {
        return awaitingCourier(query).list().stream()
                .map(entity -> toAwaitingCourier(entity, query.dateField()))
                .toList();
    }

    @Override
    public boolean assignCourier(final Delivery delivery) {
        final int updated = repository.createUpdate(DeliveryEntity.class)
                .set("supplierId.value", delivery.getSupplierId() == null ? null : delivery.getSupplierId().value())
                .set("vehicleId.value", delivery.getVehicleId() == null ? null : delivery.getVehicleId().value())
                .set("status", delivery.getStatus())
                .increment("version")
                .eq("id.id", delivery.getDeliveryId().getId())
                .eq("version", delivery.getVersion())
                .isNull("supplierId.value")
                .execute();
        if (updated == 1) {
            delivery.applyPersistedVersion(delivery.getVersion() + 1);
        }
        return updated == 1;
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveBySupplier(final SupplierId supplierId) {
        return repository.createCriteria(DeliveryEntity.class)
                .eq("supplierId.value", supplierId.value())
                .in("status", List.of(DeliveryLifecycleStatus.CREATED, DeliveryLifecycleStatus.MODIFIED))
                .count();
    }

    private Criteria<DeliveryEntity> awaitingCourier(final AwaitingCourierQuery query) {
        final String eventTimeField = eventTimeField(query.dateField());
        final Criteria<DeliveryEntity> criteria = repository.createCriteria(DeliveryEntity.class)
                .eq("destinationDepartmentId.value", query.destinationDepartmentId().getValue())
                .isNull("supplierId.value")
                .in("status", List.of(DeliveryLifecycleStatus.CREATED, DeliveryLifecycleStatus.MODIFIED))
                .eq("targetType", DeliveryTargetType.SHIPMENT)
                .eq("method", DeliveryMethod.COURIER)
                .ge(eventTimeField, query.range().fromInclusive())
                .lt(eventTimeField, query.range().toExclusive());
        if (query.after() != null) {
            final CriteriaBuilder builder = criteria.getCriteriaBuilder();
            final Root<DeliveryEntity> root = criteria.getRoot();
            criteria.and(builder.or(
                    builder.greaterThan(root.get(eventTimeField), query.after().eventTime()),
                    builder.and(builder.equal(root.get(eventTimeField), query.after().eventTime()),
                            builder.greaterThan(root.get("id").get("id"), query.after().deliveryId().getId()))));
        }
        return criteria.asc(eventTimeField).asc("id.id").maxResults(query.limit());
    }

    private AwaitingCourierDelivery toAwaitingCourier(final DeliveryEntity entity,
                                                      final CourierAssignmentDateField dateField) {
        final LocalDateTime eventTime = dateField == CourierAssignmentDateField.CREATION
                ? entity.getCreated() : entity.getCurrentDepartmentSince();
        return new AwaitingCourierDelivery(mapper.toDomain(entity),
                new DeliveryAssignmentCursor(eventTime, entity.getId()));
    }

    private static String eventTimeField(final CourierAssignmentDateField dateField) {
        return dateField == CourierAssignmentDateField.CREATION ? "created" : "currentDepartmentSince";
    }

    @Override
    public void createOrUpdate(final Delivery delivery) {
        final DeliveryEntity entity = mapper.toEntity(delivery);
        if (deliveryExists(delivery.getDeliveryId())) {
            delivery.applyPersistedVersion(repository.updateAndFlush(entity).getVersion());
        } else {
            repository.create(entity);
            delivery.applyPersistedVersion(entity.getVersion());
        }
    }

    private boolean deliveryExists(final DeliveryId deliveryId) {
        return deliveryId != null && repository.createCriteria(DeliveryEntity.class)
                .eq("id.id", deliveryId.getId())
                .one()
                .isPresent();
    }

}
