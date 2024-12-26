package com.warehouse.delivery.domain.model;

import java.util.ArrayList;
import java.util.List;


public class DeliveryMissedRequest {
    private List<DeliveryMissedDetails> deliveryMissedDetails;

    public DeliveryMissedRequest(final List<DeliveryMissedDetails> deliveryMissedDetails) {
        this.deliveryMissedDetails = deliveryMissedDetails;
    }

    public List<DeliveryMissedDetails> getDeliveryMissedDetails() {
        if (deliveryMissedDetails == null) {
            deliveryMissedDetails = new ArrayList<>();
        }
        return deliveryMissedDetails;
    }

    public void addDeliveryMissedDetails(final DeliveryMissedDetails deliveryMissedDetails) {
        getDeliveryMissedDetails().add(deliveryMissedDetails);
    }
}
