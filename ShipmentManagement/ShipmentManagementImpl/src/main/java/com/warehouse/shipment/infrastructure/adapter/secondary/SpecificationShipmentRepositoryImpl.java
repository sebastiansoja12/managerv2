package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Collection;
import java.util.List;

import com.warehouse.commonassets.repository.Criteria;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPersistenceMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SpecificationShipmentRepositoryImpl
        implements SpecificationRepository<ShipmentSearchCriteria, Shipment> {

    private final OperatorFilteredRepository<ShipmentReadEntity> repository;
    private final ShipmentPersistenceMapper persistenceMapper;

    public SpecificationShipmentRepositoryImpl(final OperatorFilteredRepository<ShipmentReadEntity> repository,
                                                final ShipmentPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public List<Shipment> list(final ShipmentSearchCriteria criteria) {
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

        if (criteria.hasDangerousGoods() != null) {
            if (criteria.hasDangerousGoods()) {
                shipmentCriteria.isNotNull("dangerousGood.unNumber");
            } else {
                shipmentCriteria.isNull("dangerousGood.unNumber");
            }
        }

        if (hasText(criteria.unNumber())) {
            shipmentCriteria.eq("dangerousGood.unNumber", criteria.unNumber().trim().toUpperCase());
        }

        if (hasText(criteria.hazardClass())) {
            shipmentCriteria.eq("dangerousGood.hazardClass", criteria.hazardClass().trim());
        }

        if (hasText(criteria.regulationType())) {
            shipmentCriteria.eq("dangerousGood.regulationType", criteria.regulationType().trim().toUpperCase());
        }

        if (hasText(criteria.transportMode())) {
            shipmentCriteria.eq("dangerousGood.transportMode", criteria.transportMode().trim().toUpperCase());
        }

        return shipmentCriteria
                .desc("createdAt")
                .firstResult(criteria.pageNumber() * criteria.pageSize())
                .maxResults(criteria.pageSize())
                .list()
                .stream()
                .map(this.persistenceMapper::toDomain)
                .toList();
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
