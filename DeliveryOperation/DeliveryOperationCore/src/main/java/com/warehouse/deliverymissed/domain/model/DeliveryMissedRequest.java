package com.warehouse.deliverymissed.domain.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.vo.DeviceInformation;


public class DeliveryMissedRequest {
    private Set<DeliveryMissedDetails> deliveryMissedDetails;
    private final DeviceInformation deviceInformation;
    private final DeliveryStatus deliveryStatus = DeliveryStatus.UNAVAILABLE;

    public DeliveryMissedRequest(final Set<DeliveryMissedDetails> deliveryMissedDetails,
                                 final DeviceInformation deviceInformation) {
        this.deliveryMissedDetails = deliveryMissedDetails;
        this.deviceInformation = deviceInformation;
    }

    public Set<DeliveryMissedDetails> getDeliveryMissedDetails() {
        if (CollectionUtils.isEmpty(deliveryMissedDetails)) {
            deliveryMissedDetails = new HashSet<>();
        }
        return deliveryMissedDetails;
    }

    public void addDetail(final DeliveryMissedDetails deliveryMissedDetails) {
        getDeliveryMissedDetails().add(deliveryMissedDetails);
    }

    public void setDeliveryMissedDetails(final Set<DeliveryMissedDetails> deliveryMissedDetails) {
        this.deliveryMissedDetails = deliveryMissedDetails;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }
}
