package com.warehouse.redirect.domain.vo;


import com.warehouse.commonassets.enumeration.ParcelStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.enumeration.Size;
import com.warehouse.redirect.domain.model.Recipient;
import com.warehouse.redirect.domain.model.Sender;

import lombok.Value;

@Value
public class RedirectParcel {

    Sender sender;

    Recipient recipient;

    Size parcelSize;

    ParcelStatus status;

    ShipmentType shipmentType;

    Long parcelRelatedId;

    String destination;

    public boolean isParent() {
        return shipmentType.equals(ShipmentType.PARENT);
    }

    public boolean hasStatusCreated() {
        return status.equals(ParcelStatus.CREATED);
    }

    public boolean isRequiredToRedirect() {
        return hasStatusCreated() && isParent();
    }
}
