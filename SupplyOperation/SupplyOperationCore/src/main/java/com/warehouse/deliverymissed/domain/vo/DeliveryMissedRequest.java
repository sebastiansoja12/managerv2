package com.warehouse.deliverymissed.domain.vo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.warehouse.commonassets.enumeration.DeliveryStatus;
import com.warehouse.delivery.domain.vo.DeviceInformation;


public class DeliveryMissedRequest {
    private Set<DeliveryMissedDetail> deliveryMissedDetails;
    private final List<DeliveryMissedInformation> deliveryMissedInformations;
    private final DeviceInformation deviceInformation;
    private final DeliveryStatus deliveryStatus = DeliveryStatus.UNAVAILABLE;

    public DeliveryMissedRequest(final Set<DeliveryMissedDetail> deliveryMissedDetails,
                                 final List<DeliveryMissedInformation> deliveryMissedInformations,
                                 final DeviceInformation deviceInformation) {
        this.deliveryMissedDetails = deliveryMissedDetails;
        this.deliveryMissedInformations = deliveryMissedInformations;
        this.deviceInformation = deviceInformation;
    }

    public List<DeliveryMissedInformation> getDeliveryMissedInformations() {
        return deliveryMissedInformations;
    }

    public Set<DeliveryMissedDetail> getDeliveryMissedDetails() {
        if (CollectionUtils.isEmpty(deliveryMissedDetails)) {
            deliveryMissedDetails = new HashSet<>();
        }
        return deliveryMissedDetails;
    }

    public void addDetail(final DeliveryMissedDetail deliveryMissedDetail) {
        getDeliveryMissedDetails().add(deliveryMissedDetail);
    }

    public void setDeliveryMissedDetails(final Set<DeliveryMissedDetail> deliveryMissedDetails) {
        this.deliveryMissedDetails = deliveryMissedDetails;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }
}
