package com.warehouse.routelogger.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.routelogger.dto.RejectTrackerRequestDto;



public class RejectTrackerRequest {
    private final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails;

    public RejectTrackerRequest(final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails) {
        this.rejectTrackerRequestDetails = rejectTrackerRequestDetails;
    }

    public static RejectTrackerRequest from(final RejectTrackerRequestDto rejectTrackerRequest) {
        final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails = rejectTrackerRequest.rejectTrackerRequestDetails()
                .stream()
                .map(detail -> new RejectTrackerRequestDetails(
                        new ShipmentId(detail.shipmentId().value()),
                        new ShipmentId(detail.shipmentId().value()),
                        new RejectReason(detail.rejectReason().value()),
                        ShipmentStatus.RETURN,
                        ProcessType.valueOf(detail.processType().name())))
                .toList();

        return new RejectTrackerRequest(rejectTrackerRequestDetails);
    }

    public List<RejectTrackerRequestDetails> getRejectTrackerRequestDetails() {
        return rejectTrackerRequestDetails;
    }
}
