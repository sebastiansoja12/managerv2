package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.logisticsdelivery.application.port.primary.command.DeliveryReadFilter;
import com.warehouse.logisticsdelivery.application.port.primary.command.DeliveryReadSortField;
import com.warehouse.logisticsdelivery.application.port.primary.command.SearchDeliveriesCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadItem;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryReadPage;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryReadModelRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryLifecycleStatus;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DeliveryReadEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DeliveryReadModelMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeliveryReadModelRepositoryImpl implements DeliveryReadModelRepository {

    private final OperatorFilteredRepository<DeliveryReadEntity> repository;
    private final DeliveryReadModelMapper mapper;

    public DeliveryReadModelRepositoryImpl(final OperatorFilteredRepository<DeliveryReadEntity> repository,
                                           final DeliveryReadModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void sync(final DeliverySnapshot delivery) {
        final DeliveryReadEntity entity = this.mapper.toEntity(delivery);
        if (exists(delivery.deliveryId())) {
            this.repository.update(entity);
        } else {
            this.repository.create(entity);
        }
    }

    @Override
    public DeliveryReadPage search(final SearchDeliveriesCommand command) {
        final DeliveryReadFilter filter = command.filter();
        final Criteria<DeliveryReadEntity> criteria = this.repository.createCriteria(DeliveryReadEntity.class);
        final CriteriaBuilder criteriaBuilder = criteria.getCriteriaBuilder();
        final Root<DeliveryReadEntity> root = criteria.getRoot();
        if (!filter.shipmentIds().isEmpty()) {
            criteria.in("shipmentId.value", filter.shipmentIds().stream().map(ShipmentId::getValue).toList());
        }
        if (filter.text() != null) {
            final String pattern = "%" + filter.text().toLowerCase(Locale.ROOT) + "%";
            criteria.and(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("id").get("id")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("targetId")), pattern)));
        }
        if (!filter.lifecycleStatuses().isEmpty()) {
            criteria.in("status", filter.lifecycleStatuses());
        }
        final List<DeliveryLifecycleStatus> hiddenStatuses = hiddenStatuses(filter);
        if (!hiddenStatuses.isEmpty()) {
            criteria.notIn("status", hiddenStatuses);
        }
        if (!filter.deliveryStatuses().isEmpty()) {
            criteria.in("deliveryStatus", filter.deliveryStatuses());
        }
        if (filter.type() != null) {
            criteria.eq("type", filter.type());
        }
        if (filter.targetType() != null) {
            criteria.eq("targetType", filter.targetType());
        }
        if (filter.method() != null) {
            criteria.eq("method", filter.method());
        }
        if (filter.departmentId() != null) {
            criteria.and(criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("originDepartmentId").get("value"), filter.departmentId().getValue()),
                    criteriaBuilder.equal(root.get("destinationDepartmentId").get("value"),
                            filter.departmentId().getValue())));
        }
        if (filter.createdFrom() != null) {
            criteria.ge("created", filter.createdFrom().atStartOfDay());
        }
        if (filter.createdTo() != null) {
            criteria.lt("created", filter.createdTo().plusDays(1).atStartOfDay());
        }
        final Expression<?> sortExpression = sortPath(root, command.sort().field());
        criteria.build().orderBy(
                command.sort().ascending() ? criteriaBuilder.asc(sortExpression) : criteriaBuilder.desc(sortExpression),
                criteriaBuilder.desc(root.get("created")));
        final List<DeliveryReadItem> items = criteria
                .firstResult(command.page() * command.size())
                .maxResults(command.size() + 1)
                .list().stream()
                .map(this.mapper::toItem)
                .toList();
        final boolean hasNext = items.size() > command.size();
        return new DeliveryReadPage(hasNext ? items.subList(0, command.size()) : items, command.page(),
                command.size(), hasNext);
    }

    private List<DeliveryLifecycleStatus> hiddenStatuses(final DeliveryReadFilter filter) {
        final List<DeliveryLifecycleStatus> hidden = new ArrayList<>();
        if (filter.hideCanceled()) {
            hidden.add(DeliveryLifecycleStatus.CANCELED);
        }
        if (filter.hideCompleted()) {
            hidden.add(DeliveryLifecycleStatus.COMPLETED);
        }
        return hidden;
    }

    private Expression<?> sortPath(final Root<DeliveryReadEntity> root, final DeliveryReadSortField field) {
        return switch (field) {
            case CREATED -> root.get("created");
            case DELIVERED_AT -> root.get("deliveredAt");
            case SHIPMENT -> root.get("shipmentId").get("value");
            case TARGET -> root.get("targetId");
            case TYPE -> root.get("type");
            case DELIVERY_STATUS -> root.get("deliveryStatus");
            case LIFECYCLE_STATUS -> root.get("status");
            case METHOD -> root.get("method");
            case STEP_COUNT -> root.get("stepCount");
        };
    }

    private boolean exists(final DeliveryId deliveryId) {
        return this.repository.createCriteria(DeliveryReadEntity.class)
                .eq("id.id", deliveryId.getId())
                .maxResults(1)
                .one()
                .isPresent();
    }
}
