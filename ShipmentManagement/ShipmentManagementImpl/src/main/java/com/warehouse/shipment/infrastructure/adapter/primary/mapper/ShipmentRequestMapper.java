package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.application.port.primary.command.ShipmentDeliveryCommand;
import com.warehouse.shipment.application.port.primary.command.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.Party;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.CustomerReference;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.application.port.primary.command.ShipmentStatusRequest;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentCreateCommand map(final ShipmentCreateRequestApi requestDto);

    default ShipmentCreateCommand mapCreateRequest(final ShipmentCreateRequestApi shipmentRequest) {
        final ShipmentCreateCommand command = map(shipmentRequest);
        command.setDeliveryPickupPointId(shipmentRequest.deliveryPickupPointId());
        return command;
    }

    default Money map(final MoneyApi money) {
        if (money == null) {
            return null;
        }
        return new Money(money.getAmount(), Currency.valueOf(money.getCurrency()));
    }

    default Dimensions map(final DimensionsApi dimensions) {
        if (dimensions == null) {
            return null;
        }
        return new Dimensions(dimensions.length(), dimensions.width(), dimensions.height(),
                dimensions.unit() == null ? null : LengthUnit.valueOf(dimensions.unit().name()));
    }

    default Weight map(final WeightApi weight) {
        if (weight == null) {
            return null;
        }
        return new Weight(weight.value(), weight.unit() == null ? null : WeightUnit.valueOf(weight.unit().name()));
    }

    default CustomerReference mapCustomerReference(final String customerReference) {
        return customerReference == null ? null : new CustomerReference(customerReference);
    }

    default ShipmentServiceLevel map(final ShipmentServiceLevelDto serviceLevel) {
        return serviceLevel == null ? null : ShipmentServiceLevel.valueOf(serviceLevel.name());
    }

    default PackagingType map(final PackagingTypeDto packagingType) {
        return packagingType == null ? null : PackagingType.valueOf(packagingType.name());
    }

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    Party mapToParty(final PersonApi person);

    default ShipmentStatusRequest map(final ShipmentStatusRequestApi shipmentStatusRequest) {
        return new ShipmentStatusRequest(
                new ShipmentId(shipmentStatusRequest.shipmentId().getValue()),
                ShipmentStatus.valueOf(shipmentStatusRequest.shipmentStatus().name()));
    }

    default SignatureChangeRequest map(final SignatureChangeRequestApi signatureChangeRequest) {
        final ShipmentId shipmentId = new ShipmentId(signatureChangeRequest.shipmentId().getValue());
        return new SignatureChangeRequest(shipmentId, signatureChangeRequest.signature(), signatureChangeRequest.signerName(),
                signatureChangeRequest.documentReference());
    }

    default ShipmentDeliveryCommand map(final ShipmentDeliveryRequestApiDto deliveryRequest) {
        return new ShipmentDeliveryCommand(new ShipmentId(deliveryRequest.shipmentId().getValue()),
                DeliveryMethod.valueOf(deliveryRequest.deliveryMethod()), new SupplierCode(deliveryRequest.supplierCode().value()),
                DeliveryStatus.valueOf(deliveryRequest.deliveryStatus()));
    }

    default ShipmentSearchCriteria map(final ShipmentSearchRequestApi request) {
        if (request == null) {
            return new ShipmentSearchCriteria(
                    null, null, List.of(), List.of(), null, null, null,
                    null, null, null, null, null, null, null, null
            );
        }

        return new ShipmentSearchCriteria(
                request.shipmentId(),
                request.trackingNumber(),
                mapStatuses(request.shipmentStatuses()),
                mapPriorities(request.shipmentPriorities()),
                request.senderName(),
                request.recipientName(),
                request.destination(),
                request.minPrice(),
                request.maxPrice(),
                request.currency() == null || request.currency().isBlank() ? null : Currency.valueOf(request.currency()),
                request.locked(),
                request.createdFrom(),
                request.createdTo(),
                request.page(),
                request.size()
        );
    }

    private List<ShipmentStatus> mapStatuses(final List<ShipmentStatusDto> statuses) {
        return statuses == null ? List.of() : statuses.stream()
                .map(status -> ShipmentStatus.valueOf(status.name()))
                .toList();
    }

    private List<ShipmentPriority> mapPriorities(final List<ShipmentPriorityDto> priorities) {
        return priorities == null ? List.of() : priorities.stream()
                .map(priority -> ShipmentPriority.valueOf(priority.name()))
                .toList();
    }
}
