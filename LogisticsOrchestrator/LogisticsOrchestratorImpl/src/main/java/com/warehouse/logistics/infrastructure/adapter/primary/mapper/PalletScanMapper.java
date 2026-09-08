package com.warehouse.logistics.infrastructure.adapter.primary.mapper;

import com.warehouse.logistics.application.port.primary.command.PalletScanCommand;
import com.warehouse.logistics.application.port.primary.result.PalletScanDetail;
import com.warehouse.logistics.application.port.primary.result.PalletScanOutcome;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.terminal.jaxb.PalletScanRequest;
import com.warehouse.terminal.jaxb.PalletScanResponse;
import com.warehouse.terminal.jaxb.PalletScanResponseDetailType;
import com.warehouse.terminal.jaxb.PalletScanResponseDetailsType;

public class PalletScanMapper {

    public PalletScanCommand map(final ProcessId processId, final PalletScanRequest palletScanRequest) {
        final String trackingNumber = palletScanRequest.getPalletTrackingNumber();
        return new PalletScanCommand(processId, trackingNumber == null ? null : trackingNumber.trim());
    }

    public PalletScanResponse map(final ProcessId processId, final PalletScanOutcome outcome) {
        final PalletScanResponseDetailsType details = new PalletScanResponseDetailsType();
        outcome.details().stream()
                .map(this::map)
                .forEach(details.getPalletScanResponseDetail()::add);

        final PalletScanResponse response = new PalletScanResponse();
        response.setProcessId(processId.value().toString());
        response.setSuccess(outcome.success());
        response.setPalletTrackingNumber(outcome.trackingNumber());
        response.setPalletID(outcome.palletId());
        response.setPalletStatus(outcome.palletStatus());
        response.setAlreadyLoaded(outcome.alreadyLoaded());
        response.setErrorCode(outcome.errorCode());
        response.setErrorMessage(outcome.errorMessage());
        response.setPalletScanResponseDetails(details);
        return response;
    }

    private PalletScanResponseDetailType map(final PalletScanDetail detail) {
        final PalletScanResponseDetailType responseDetail = new PalletScanResponseDetailType();
        responseDetail.setShipmentID(detail.shipmentId().getValue());
        responseDetail.setSuccess(detail.success());
        responseDetail.setShipmentStatus(detail.shipmentStatus());
        responseDetail.setErrorMessage(detail.errorMessage());
        return responseDetail;
    }
}
