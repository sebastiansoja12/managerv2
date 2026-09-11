package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.primary.command.ShipmentHandoverCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentHandoverResult;
import com.warehouse.terminal.jaxb.HandoverDetailType;
import com.warehouse.terminal.jaxb.HandoverDetailsType;
import com.warehouse.terminal.jaxb.HandoverRequest;
import com.warehouse.terminal.jaxb.HandoverResponse;
import com.warehouse.terminal.jaxb.HandoverResponseDetailType;
import com.warehouse.terminal.jaxb.HandoverResponseDetailsType;

public class HandoverMapper {

    public ShipmentHandoverCommand map(final ProcessId processId, final HandoverRequest handoverRequest) {
        final HandoverDetailsType handoverDetails = handoverRequest.getHandoverDetails();
        if (handoverDetails == null) {
            return new ShipmentHandoverCommand(processId, List.of(), null, null);
        }
        final List<TrackingNumber> trackingNumbers = handoverDetails.getHandoverDetail()
                .stream()
                .map(this::map)
                .toList();
        return new ShipmentHandoverCommand(processId, trackingNumbers, trim(handoverDetails.getRecipientName()),
                handoverDetails.getSignature());
    }

    public HandoverResponse map(final ProcessId processId, final ShipmentHandoverResult result) {
        final HandoverResponseDetailsType details = new HandoverResponseDetailsType();
        result.details()
                .stream()
                .map(this::map)
                .forEach(details.getHandoverResponseDetail()::add);

        final HandoverResponse handoverResponse = new HandoverResponse();
        handoverResponse.setProcessId(processId.value().toString());
        handoverResponse.setSuccess(result.allHandedOver());
        handoverResponse.setHandoverResponseDetails(details);
        return handoverResponse;
    }

    private TrackingNumber map(final HandoverDetailType handoverDetail) {
        return new TrackingNumber(trim(handoverDetail.getTrackingNumber()));
    }

    private HandoverResponseDetailType map(final ShipmentHandoverDetail detail) {
        final HandoverResponseDetailType responseDetail = new HandoverResponseDetailType();
        responseDetail.setTrackingNumber(detail.trackingNumber().value());
        responseDetail.setSuccess(detail.handedOver());
        responseDetail.setErrorMessage(detail.errorMessage());
        return responseDetail;
    }

    private String trim(final String value) {
        return value == null ? null : value.trim();
    }
}
