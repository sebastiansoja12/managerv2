package com.warehouse.logistics.domain.model;

import java.util.List;

public class DeliveryReturnRequest {
    private List<DeliveryReturnDetails> deliveryReturnDetails;

    public DeliveryReturnRequest(final List<DeliveryReturnDetails> deliveryReturnDetails) {
        this.deliveryReturnDetails = deliveryReturnDetails;
    }

    public List<DeliveryReturnDetails> getDeliveryReturnDetails() {
        return deliveryReturnDetails;
    }

    public void setDeliveryReturnDetails(final List<DeliveryReturnDetails> deliveryReturnDetails) {
        this.deliveryReturnDetails = deliveryReturnDetails;
    }
}
