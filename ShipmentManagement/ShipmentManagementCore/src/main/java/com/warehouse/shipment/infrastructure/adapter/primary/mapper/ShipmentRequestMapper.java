package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.ShipmentUpdateType;
import com.warehouse.shipment.domain.model.ShipmentCreateCommand;
import com.warehouse.shipment.domain.model.ShipmentDeliveryCommand;
import com.warehouse.shipment.domain.model.ShipmentUpdateCommand;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentConfiguration;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.domain.vo.ShipmentStatusRequest;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ShipmentRequestMapper {

    ShipmentCreateCommand map(final ShipmentCreateRequestApi requestDto);

    default Money map(final MoneyApi money) {
        if (money == null) {
            return null;
        }
        return new Money(money.getAmount(), Currency.valueOf(money.getCurrency()));
    }

    ShipmentUpdateCommand map(final ShipmentUpdateRequestApi request);

    ShipmentConfiguration map(final ShipmentConfigurationApi configuration);

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    ShipmentUpdateType map(final ShipmentUpdateTypeApi shipmentUpdateType);
    
    Sender mapToSender(final PersonApi person);

    Recipient mapToRecipient(final PersonApi person);

    default ShipmentStatusRequest map(final ShipmentStatusRequestApi shipmentStatusRequest) {
        return new ShipmentStatusRequest(new ShipmentId(shipmentStatusRequest.shipmentId().getValue()), ShipmentStatus.valueOf(shipmentStatusRequest.shipmentStatus().name()));
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
            return new ShipmentSearchCriteria(null, null, List.of(), List.of(), List.of(), null, null, null,
                    null, null, null, null, null, null, null, null);
        }

        return new ShipmentSearchCriteria(
                request.shipmentId(),
                request.trackingNumber(),
                mapStatuses(request.shipmentStatuses()),
                mapSizes(request.shipmentSizes()),
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

    private List<ShipmentSize> mapSizes(final List<ShipmentSizeDto> sizes) {
        return sizes == null ? List.of() : sizes.stream()
                .map(size -> ShipmentSize.valueOf(size.name()))
                .toList();
    }

    private List<ShipmentPriority> mapPriorities(final List<ShipmentPriorityDto> priorities) {
        return priorities == null ? List.of() : priorities.stream()
                .map(priority -> ShipmentPriority.valueOf(priority.name()))
                .toList();
    }
}
