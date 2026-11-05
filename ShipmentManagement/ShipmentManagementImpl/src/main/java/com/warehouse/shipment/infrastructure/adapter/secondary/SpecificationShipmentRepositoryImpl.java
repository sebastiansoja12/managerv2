package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Collection;
import java.util.List;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.domain.vo.ShipmentListEntry;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SpecificationShipmentRepositoryImpl
        implements SpecificationRepository<ShipmentSearchCriteria, ShipmentListEntry> {

    private final OperatorFilteredRepository<ShipmentReadEntity> repository;
    private final ShipmentPersistenceMapper persistenceMapper;

    public SpecificationShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentReadEntity> repository,
                                                final ShipmentPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public List<ShipmentListEntry> list(final ShipmentSearchCriteria criteria) {
        return filteredCriteria(criteria)
                .desc("createdAt")
                .firstResult(criteria.pageNumber() * criteria.pageSize())
                .maxResults(criteria.pageSize())
                .list()
                .stream()
                .map(this::toListEntry)
                .toList();
    }

    @Override
    public long count(final ShipmentSearchCriteria criteria) {
        return filteredCriteria(criteria).count();
    }

    private Criteria<ShipmentReadEntity> filteredCriteria(final ShipmentSearchCriteria criteria) {
        final Criteria<ShipmentReadEntity> shipmentCriteria = repository.createCriteria(ShipmentReadEntity.class);

        if (criteria.shipmentId() != null) {
            shipmentCriteria.eq("shipmentId.value", criteria.shipmentId());
        }

        if (hasText(criteria.trackingNumber())) {
            shipmentCriteria.eq("trackingNumber.value", criteria.trackingNumber().trim());
        }

        if (hasElements(criteria.shipmentStatuses())) {
            shipmentCriteria.in("shipmentStatus", criteria.shipmentStatuses());
        }


        if (hasElements(criteria.shipmentPriorities())) {
            shipmentCriteria.in("shipmentPriority", criteria.shipmentPriorities());
        }

        if (hasText(criteria.senderName())) {
            shipmentCriteria.and(nameLike(shipmentCriteria, criteria.senderName(), "firstName", "lastName"));
        }

        if (hasText(criteria.recipientName())) {
            shipmentCriteria.and(nameLike(
                    shipmentCriteria,
                    criteria.recipientName(),
                    "recipientFirstName",
                    "recipientLastName"
            ));
        }

        if (criteria.minPrice() != null) {
            shipmentCriteria.ge("price.amount", criteria.minPrice());
        }

        if (criteria.maxPrice() != null) {
            shipmentCriteria.le("price.amount", criteria.maxPrice());
        }

        if (criteria.currency() != null) {
            shipmentCriteria.eq("price.currency", criteria.currency());
        }

        if (criteria.locked() != null) {
            shipmentCriteria.eq("locked", criteria.locked());
        }

        if (criteria.createdFrom() != null) {
            shipmentCriteria.ge("createdAt", criteria.createdFrom());
        }

        if (criteria.createdTo() != null) {
            shipmentCriteria.le("createdAt", criteria.createdTo());
        }

        if (criteria.departmentId() != null) {
            shipmentCriteria.and(departmentMatches(shipmentCriteria, criteria.departmentId()));
        }

        return shipmentCriteria;
    }

    private ShipmentListEntry toListEntry(final ShipmentReadEntity entity) {
        return new ShipmentListEntry(persistenceMapper.toDomain(entity), persistenceMapper.deliveryDetails(entity));
    }

    private Predicate departmentMatches(final Criteria<ShipmentReadEntity> criteria, final DepartmentId departmentId) {
        final CriteriaBuilder cb = criteria.getCriteriaBuilder();
        final Root<ShipmentReadEntity> root = criteria.getRoot();
        return cb.or(
                cb.equal(root.get("originDepartmentId").get("value"), departmentId.value()),
                cb.equal(root.get("destinationDepartmentId").get("value"), departmentId.value())
        );
    }

    private Predicate nameLike(
            final Criteria<ShipmentReadEntity> criteria,
            final String value,
            final String firstNameField,
            final String lastNameField
    ) {
        final CriteriaBuilder cb = criteria.getCriteriaBuilder();
        final Root<ShipmentReadEntity> root = criteria.getRoot();
        final String pattern = like(value);

        return cb.or(
                cb.like(cb.lower(root.get(firstNameField)), pattern),
                cb.like(cb.lower(root.get(lastNameField)), pattern),
                cb.like(
                        cb.lower(cb.concat(cb.concat(root.get(firstNameField), " "), root.get(lastNameField))),
                        pattern
                )
        );
    }

    private boolean hasText(final String value) {
        return value != null && !value.isBlank();
    }

    private boolean hasElements(final Collection<?> values) {
        return values != null && !values.isEmpty();
    }

    private String like(final String value) {
        return "%" + value.trim().toLowerCase() + "%";
    }
}
