package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import java.util.List;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.terminal.jaxb.CollectDetailType;
import com.warehouse.terminal.jaxb.CollectRequest;
import com.warehouse.terminal.jaxb.CollectResponse;
import com.warehouse.terminal.jaxb.CollectResponseDetailType;
import com.warehouse.terminal.jaxb.CollectResponseDetailsType;

public class CollectMapper {

    public ShipmentCollectCommand map(final ProcessId processId, final CollectRequest collectRequest) {
        final List<TrackingNumber> trackingNumbers = collectRequest.getCollectDetails() == null
                ? List.of()
                : collectRequest.getCollectDetails().getCollectDetail()
                        .stream()
                        .map(this::map)
                        .toList();
        return new ShipmentCollectCommand(processId, trackingNumbers);
    }

    public CollectResponse map(final ProcessId processId, final ShipmentCollectResult result) {
        final CollectResponseDetailsType details = new CollectResponseDetailsType();
        result.details()
                .stream()
                .map(this::map)
                .forEach(details.getCollectResponseDetail()::add);

        final CollectResponse collectResponse = new CollectResponse();
        collectResponse.setProcessId(processId.value().toString());
        collectResponse.setSuccess(result.allCollected());
        collectResponse.setCollectResponseDetails(details);
        return collectResponse;
    }

    private TrackingNumber map(final CollectDetailType collectDetail) {
        final String trackingNumber = collectDetail.getTrackingNumber();
        return new TrackingNumber(trackingNumber == null ? null : trackingNumber.trim());
    }

    private CollectResponseDetailType map(final ShipmentCollectDetail detail) {
        final CollectResponseDetailType responseDetail = new CollectResponseDetailType();
        responseDetail.setTrackingNumber(detail.trackingNumber().value());
        responseDetail.setSuccess(detail.collected());
        responseDetail.setErrorMessage(detail.errorMessage());
        return responseDetail;
    }
}
