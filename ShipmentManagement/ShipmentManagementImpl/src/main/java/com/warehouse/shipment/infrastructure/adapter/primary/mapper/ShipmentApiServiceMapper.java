package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentRerouteResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetail;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.infrastructure.dto.ShipmentDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentDocumentDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentRerouteDetailsDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentTrackingEventDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Component
public class ShipmentApiServiceMapper {

    public ShipmentDetailsDto map(final ShipmentResult shipment) {
        return new ShipmentDetailsDto(
                shipment.snapshot().shipmentId(),
                shipment.snapshot().shipmentStatus(),
                shipment.snapshot().shipmentRelatedId(),
                weightInKilograms(shipment.snapshot().weight()),
                shipment.snapshot().trackingNumber() == null ? null : shipment.snapshot().trackingNumber().value(),
                shipment.snapshot().recipient() == null ? null : shipment.snapshot().recipient().getCity(),
                shipment.snapshot().recipient() == null ? null : shipment.snapshot().recipient().getPostalCode()
        );
    }

    public ShipmentDocumentDto mapDocument(final ShipmentResult shipment) {
        final ShipmentSnapshot snapshot = shipment.snapshot();
        return new ShipmentDocumentDto(
                snapshot.shipmentId(),
                snapshot.shipmentRelatedId(),
                snapshot.shipmentStatus(),
                snapshot.trackingNumber() == null ? null : snapshot.trackingNumber().value(),
                weightInKilograms(snapshot.weight()),
                snapshot.sender() == null ? null : map(snapshot.sender()),
                snapshot.recipient() == null ? null : map(snapshot.recipient())
        );
    }

    public ShipmentTrackingDto mapTracking(final ShipmentRouteLog shipmentRouteLog) {
        final ShipmentSnapshot snapshot = shipmentRouteLog.shipment().snapshot();
        return new ShipmentTrackingDto(
                snapshot.shipmentId(),
                snapshot.trackingNumber() == null ? null : snapshot.trackingNumber().value(),
                snapshot.shipmentStatus(),
                snapshot.createdAt(),
                snapshot.updatedAt(),
                snapshot.sender() == null ? null : map(snapshot.sender()),
                snapshot.recipient() == null ? null : map(snapshot.recipient()),
                mapEvents(shipmentRouteLog.routeLog())
        );
    }

    private List<ShipmentTrackingEventDto> mapEvents(final RouteLogRecord routeLog) {
        if (routeLog == null || routeLog.routeLogRecordDetails() == null
                || routeLog.routeLogRecordDetails().routeLogRecordDetailSet() == null) {
            return List.of();
        }
        return routeLog.routeLogRecordDetails().routeLogRecordDetailSet().stream()
                .sorted(Comparator.comparing(RouteLogRecordDetail::timestamp,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(detail -> new ShipmentTrackingEventDto(
                        detail.timestamp(),
                        detail.shipmentStatus(),
                        detail.processType(),
                        routeLog.processId(),
                        detail.departmentCode(),
                        detail.username(),
                        detail.description()))
                .toList();
    }

    private BigDecimal weightInKilograms(final Weight weight) {
        if (weight == null) {
            return null;
        }
        return WeightUnit.G.equals(weight.unit()) ? weight.value().movePointLeft(3) : weight.value();
    }

    public ShipmentRerouteDetailsDto map(final ShipmentRerouteResult shipmentRerouteResult) {
        final ShipmentSnapshot snapshot = shipmentRerouteResult.snapshot();
        return new ShipmentRerouteDetailsDto(
                snapshot.shipmentId(),
                snapshot.trackingNumber(),
                snapshot.shipmentStatus(),
                shipmentRerouteResult.reroutable(),
                map(snapshot.sender()),
                map(snapshot.recipient())
        );
    }

    private ShipmentPartyDto map(final Party party) {
        return new ShipmentPartyDto(
                party.getFirstName(),
                party.getLastName(),
                party.getEmail(),
                party.getTelephoneNumber(),
                party.getCity(),
                party.getPostalCode(),
                party.getStreet(),
                party.getCountryCode() != null ? party.getCountryCode().name() : null
        );
    }
}
