package com.warehouse.deliveryreject.domain.vo;

import java.util.ArrayList;
import java.util.List;

import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;

public class RejectTrackerRequest {
    private final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails;

    public RejectTrackerRequest(final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails) {
        this.rejectTrackerRequestDetails = rejectTrackerRequestDetails;
    }

    public static RejectTrackerRequest from(final DeliveryRejectRequest request) {
        final List<RejectTrackerRequestDetails> rejectTrackerRequestDetails = new ArrayList<>();
        final List<DeliveryRejectDetails> deliveryRejectDetails = request.getDeliveryRejectDetails();
        for (final DeliveryRejectDetails deliveryRejectDetail : deliveryRejectDetails) {
            final RejectTrackerRequestDetails rejectTrackerRequestDetail = new RejectTrackerRequestDetails(
                    deliveryRejectDetail.getShipmentId(),
                    deliveryRejectDetail.getShipmentId(),
                    deliveryRejectDetail.getRejectReason(),
                    deliveryRejectDetail.getShipmentStatus(),
                    deliveryRejectDetail.getProcessType()
            );
            rejectTrackerRequestDetails.add(rejectTrackerRequestDetail);
        }
        return new RejectTrackerRequest(rejectTrackerRequestDetails);
    }

    public List<RejectTrackerRequestDetails> getRejectTrackerRequestDetails() {
        return rejectTrackerRequestDetails;
    }
}
