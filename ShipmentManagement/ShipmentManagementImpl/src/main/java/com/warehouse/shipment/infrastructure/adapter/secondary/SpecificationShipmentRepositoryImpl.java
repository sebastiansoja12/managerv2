package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Collection;
import java.util.List;

import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SpecificationShipmentRepositoryImpl
        implements SpecificationRepository<ShipmentSearchCriteria, Shipment> {

    private final OperatorFilteredRepository<ShipmentEntity> repository;

    public SpecificationShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentEntity> repository) {
        this.repository = repository;
    }

    @Override
    public List<Shipment> list(final ShipmentSearchCriteria criteria) {
        final Criteria<ShipmentEntity> shipmentCriteria = repository.createCriteria(ShipmentEntity.class);

        if (criteria.shipmentId() != null) {
            shipmentCriteria.eq("shipmentId.value", criteria.shipmentId());
        }

        if (hasText(criteria.trackingNumber())) {
            shipmentCriteria.eq("trackingNumber.value", criteria.trackingNumber().trim());
        }

        if (hasElements(criteria.shipmentStatuses())) {
            shipmentCriteria.in("shipmentStatus", criteria.shipmentStatuses());
        }

        if (hasElements(criteria.shipmentSizes())) {
            shipmentCriteria.in("shipmentSize", criteria.shipmentSizes());
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

        if (hasText(criteria.destination())) {
            shipmentCriteria.ilike("destination", like(criteria.destination()));
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

        return shipmentCriteria
                .desc("createdAt")
                .firstResult(criteria.pageNumber() * criteria.pageSize())
                .maxResults(criteria.pageSize())
                .list()
                .stream()
                .map(Shipment::from)
                .toList();
    }

    private Predicate nameLike(
            final Criteria<ShipmentEntity> criteria,
            final String value,
            final String firstNameField,
            final String lastNameField
    ) {
        final CriteriaBuilder cb = criteria.getCriteriaBuilder();
        final Root<ShipmentEntity> root = criteria.getRoot();
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
