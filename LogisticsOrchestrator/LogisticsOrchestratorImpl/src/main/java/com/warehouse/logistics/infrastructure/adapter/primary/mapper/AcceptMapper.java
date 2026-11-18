package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.logistics.application.port.primary.command.ShipmentAcceptCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentAcceptResult;
import com.warehouse.terminal.jaxb.AcceptDetailType;
import com.warehouse.terminal.jaxb.AcceptRequest;
import com.warehouse.terminal.jaxb.AcceptResponse;
import com.warehouse.terminal.jaxb.AcceptResponseDetailType;
import com.warehouse.terminal.jaxb.AcceptResponseDetailsType;

public class AcceptMapper {

    public ShipmentAcceptCommand map(final ProcessId processId, final AcceptRequest acceptRequest) {
        final List<TrackingNumber> trackingNumbers = acceptRequest.getAcceptDetails() == null
                ? List.of()
                : acceptRequest.getAcceptDetails().getAcceptDetail()
                        .stream()
                        .map(this::map)
                        .toList();
        return new ShipmentAcceptCommand(processId, trackingNumbers);
    }

    public AcceptResponse map(final ProcessId processId, final ShipmentAcceptResult result) {
        final AcceptResponseDetailsType details = new AcceptResponseDetailsType();
        result.details()
                .stream()
                .map(this::map)
                .forEach(details.getAcceptResponseDetail()::add);

        final AcceptResponse acceptResponse = new AcceptResponse();
        acceptResponse.setProcessId(processId.value().toString());
        acceptResponse.setSuccess(result.allAccepted());
        acceptResponse.setErrorMessage(result.failureDescription());
        acceptResponse.setAcceptResponseDetails(details);
        return acceptResponse;
    }

    private TrackingNumber map(final AcceptDetailType acceptDetail) {
        final String trackingNumber = acceptDetail.getTrackingNumber();
        return new TrackingNumber(trackingNumber == null ? null : trackingNumber.trim());
    }

    private AcceptResponseDetailType map(final ShipmentAcceptDetail detail) {
        final AcceptResponseDetailType responseDetail = new AcceptResponseDetailType();
        responseDetail.setTrackingNumber(detail.trackingNumber().value());
        responseDetail.setSuccess(detail.accepted());
        responseDetail.setErrorMessage(detail.errorMessage());
        return responseDetail;
    }
}
