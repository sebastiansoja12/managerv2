package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.ShipmentSource;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.PartyEntity;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentReadEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

final class ShipmentStatisticsFixture {

    static final OperatorId OPERATOR = new OperatorId(1L);

    static final OperatorId OTHER_OPERATOR = new OperatorId(2L);

    static final DepartmentId KATOWICE = new DepartmentId(10L);

    static final DepartmentId GLIWICE = new DepartmentId(20L);

    private static final AtomicLong SEQUENCE = new AtomicLong(1000L);

    private ShipmentStatisticsFixture() {
    }

    static ShipmentReadEntity shipment(final OperatorId operatorId,
                                       final DepartmentId originDepartmentId,
                                       final ShipmentStatus status,
                                       final LocalDateTime createdAt,
                                       final LocalDateTime deliveredAt,
                                       final String priceAmount,
                                       final Currency currency) {
        final long sequence = SEQUENCE.incrementAndGet();
        final ShipmentReadEntity entity = ShipmentReadEntity.builder()
                .shipmentId(new ShipmentId(sequence))
                .sender(party())
                .recipient(party())
                .shipmentStatus(status)
                .shipmentType(ShipmentType.PARENT)
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .locked(false)
                .shipmentPriority(ShipmentPriority.MEDIUM)
                .serviceLevel(ShipmentServiceLevel.STANDARD)
                .source(ShipmentSource.WEB)
                .price(priceAmount == null ? null : new Money(new BigDecimal(priceAmount), currency))
                .signatureRequired(false)
                .externalId(new ExternalId<>("external-" + sequence))
                .trackingNumber(new TrackingNumber("TN" + sequence))
                .originDepartmentId(originDepartmentId)
                .deliveredAt(deliveredAt)
                .incidentReported(false)
                .build();
        entity.assignOperator(operatorId);
        return entity;
    }

    private static PartyEntity party() {
        return PartyEntity.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan@example.com")
                .telephoneNumber("123456789")
                .city("Katowice")
                .street("Mariacka 1")
                .postalCode("40-001")
                .build();
    }
}
