package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.shipment.application.port.primary.result.DailyShipmentCount;
import com.warehouse.shipment.application.port.primary.result.ShipmentDeliveryFigures;
import com.warehouse.shipment.application.port.primary.result.ShipmentRegistrationFigures;
import com.warehouse.shipment.application.port.secondary.ShipmentStatisticsRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShipmentStatisticsRepositoryImpl implements ShipmentStatisticsRepository {

    private static final List<ShipmentStatus> NOT_REGISTERED_STATUSES = List.of(ShipmentStatus.PLANNED,
            ShipmentStatus.CANCELED);

    private final OperatorFilteredRepository<ShipmentReadEntity> repository;

    public ShipmentStatisticsRepositoryImpl(final OperatorFilteredRepository<ShipmentReadEntity> repository) {
        this.repository = repository;
    }

    @Override
    public List<ShipmentRegistrationFigures> registrationFigures(final LocalDateTime rangeStart,
                                                                 final LocalDateTime rangeEnd) {
        final Map<FiguresKey, List<ShipmentReadEntity>> shipments = this.repository
                .createCriteria(ShipmentReadEntity.class)
                .ge("createdAt", rangeStart)
                .lt("createdAt", rangeEnd)
                .ne("shipmentStatus", ShipmentStatus.PLANNED)
                .list()
                .stream()
                .collect(Collectors.groupingBy(FiguresKey::of, LinkedHashMap::new, Collectors.toList()));
        return shipments.entrySet().stream()
                .map(entry -> {
                    final List<ShipmentReadEntity> registered = entry.getValue().stream()
                            .filter(shipment -> !ShipmentStatus.CANCELED.equals(shipment.getShipmentStatus()))
                            .toList();
                    return new ShipmentRegistrationFigures(entry.getKey().originDepartmentId(),
                            entry.getKey().currency(), registered.size(),
                            entry.getValue().size() - registered.size(), totalPrice(registered));
                })
                .toList();
    }

    @Override
    public List<ShipmentDeliveryFigures> deliveryFigures(final LocalDateTime rangeStart,
                                                         final LocalDateTime rangeEnd) {
        final Map<FiguresKey, List<ShipmentReadEntity>> shipments = this.repository
                .createCriteria(ShipmentReadEntity.class)
                .ge("deliveredAt", rangeStart)
                .lt("deliveredAt", rangeEnd)
                .notIn("shipmentStatus", NOT_REGISTERED_STATUSES)
                .list()
                .stream()
                .collect(Collectors.groupingBy(FiguresKey::of, LinkedHashMap::new, Collectors.toList()));
        return shipments.entrySet().stream()
                .map(entry -> new ShipmentDeliveryFigures(entry.getKey().originDepartmentId(),
                        entry.getKey().currency(), entry.getValue().size(), totalPrice(entry.getValue())))
                .toList();
    }

    @Override
    public List<DailyShipmentCount> dailyRegistrations(final LocalDateTime rangeStart,
                                                       final LocalDateTime rangeEnd) {
        final Map<DailyKey, Long> counts = this.repository.createCriteria(ShipmentReadEntity.class)
                .ge("createdAt", rangeStart)
                .lt("createdAt", rangeEnd)
                .notIn("shipmentStatus", NOT_REGISTERED_STATUSES)
                .list()
                .stream()
                .collect(Collectors.groupingBy(DailyKey::of, LinkedHashMap::new, Collectors.counting()));
        return counts.entrySet().stream()
                .map(entry -> new DailyShipmentCount(entry.getKey().originDepartmentId(), entry.getKey().day(),
                        entry.getValue()))
                .toList();
    }

    @Override
    public Optional<LocalDateTime> firstRegistration() {
        return this.repository.createCriteria(ShipmentReadEntity.class)
                .notIn("shipmentStatus", NOT_REGISTERED_STATUSES)
                .asc("createdAt")
                .maxResults(1)
                .one()
                .map(ShipmentReadEntity::getCreatedAt);
    }

    private BigDecimal totalPrice(final List<ShipmentReadEntity> shipments) {
        return shipments.stream()
                .map(ShipmentReadEntity::getPrice)
                .filter(price -> price != null && price.getAmount() != null)
                .map(Money::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private record FiguresKey(DepartmentId originDepartmentId, Currency currency) {

        static FiguresKey of(final ShipmentReadEntity shipment) {
            return new FiguresKey(shipment.getOriginDepartmentId(),
                    shipment.getPrice() == null ? null : shipment.getPrice().getCurrency());
        }
    }

    private record DailyKey(DepartmentId originDepartmentId, LocalDate day) {

        static DailyKey of(final ShipmentReadEntity shipment) {
            return new DailyKey(shipment.getOriginDepartmentId(), shipment.getCreatedAt().toLocalDate());
        }
    }
}
