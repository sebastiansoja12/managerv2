package com.warehouse.deliverymissed.domain.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.terminal.DeviceInformation;


public class DeliveryMissedRequest {
    private DeliveryId deliveryId;
    private Set<DeliveryMissedDetails> deliveryMissedDetails;
    private final DeviceInformation deviceInformation;
    private final DeliveryStatus deliveryStatus = DeliveryStatus.UNAVAILABLE;

    public DeliveryMissedRequest(final DeliveryId deliveryId,
                                 final Set<DeliveryMissedDetails> deliveryMissedDetails,
                                 final DeviceInformation deviceInformation) {
        this.deliveryId = deliveryId;
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

    public DeliveryId getDeliveryId() {
        return deliveryId;
    }
}
