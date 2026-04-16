package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.warehouse.commonassets.searchobject.SpecificationRepository;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;

public class SpecificationShipmentRepositoryImpl
        implements SpecificationRepository<ShipmentSearchCriteria, Shipment> {

    private final ShipmentReadRepository repository;

    public SpecificationShipmentRepositoryImpl(final ShipmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Shipment> list(final ShipmentSearchCriteria criteria) {
        Specification<ShipmentEntity> specification = (root, query, cb) -> cb.conjunction();

        if (criteria.shipmentId() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("shipmentId").get("value"), criteria.shipmentId()));
        }

        if (hasText(criteria.trackingNumber())) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("trackingNumber").get("value"), criteria.trackingNumber().trim()));
        }

        if (hasElements(criteria.shipmentStatuses())) {
            specification = specification.and((root, query, cb) ->
                    root.get("shipmentStatus").in(criteria.shipmentStatuses()));
        }

        if (hasElements(criteria.shipmentSizes())) {
            specification = specification.and((root, query, cb) ->
                    root.get("shipmentSize").in(criteria.shipmentSizes()));
        }

        if (hasElements(criteria.shipmentPriorities())) {
            specification = specification.and((root, query, cb) ->
                    root.get("shipmentPriority").in(criteria.shipmentPriorities()));
        }

        if (hasText(criteria.senderName())) {
            specification = specification.and(nameLike(criteria.senderName(), "firstName", "lastName"));
        }

        if (hasText(criteria.recipientName())) {
            specification = specification.and(nameLike(
                    criteria.recipientName(),
                    "recipientFirstName",
                    "recipientLastName"
            ));
        }

        if (hasText(criteria.destination())) {
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("destination")), like(criteria.destination())));
        }

        if (criteria.minPrice() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price").get("amount"), criteria.minPrice()));
        }

        if (criteria.maxPrice() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price").get("amount"), criteria.maxPrice()));
        }

        if (criteria.currency() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("price").get("currency"), criteria.currency()));
        }

        if (criteria.locked() != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("locked"), criteria.locked()));
        }

        if (criteria.createdFrom() != null) {
            specification = specification.and(createdAfterOrEqual(criteria.createdFrom()));
        }

        if (criteria.createdTo() != null) {
            specification = specification.and(createdBeforeOrEqual(criteria.createdTo()));
        }

        final PageRequest pageRequest = PageRequest.of(
                criteria.pageNumber(),
                criteria.pageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return repository.findAll(specification, pageRequest).stream()
                .map(Shipment::from)
                .toList();
    }

    private Specification<ShipmentEntity> nameLike(
            final String value,
            final String firstNameField,
            final String lastNameField
    ) {
        return (root, query, cb) -> {
            final String pattern = like(value);

            return cb.or(
                    cb.like(cb.lower(root.get(firstNameField)), pattern),
                    cb.like(cb.lower(root.get(lastNameField)), pattern),
                    cb.like(
                            cb.lower(cb.concat(cb.concat(root.get(firstNameField), " "), root.get(lastNameField))),
                            pattern
                    )
            );
        };
    }

    private Specification<ShipmentEntity> createdAfterOrEqual(final LocalDateTime createdFrom) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom);
    }

    private Specification<ShipmentEntity> createdBeforeOrEqual(final LocalDateTime createdTo) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), createdTo);
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