package com.warehouse.redirect.domain.vo;


import com.warehouse.commonassets.ParcelStatus;
import com.warehouse.commonassets.ParcelType;
import com.warehouse.commonassets.Size;
import com.warehouse.redirect.domain.model.Recipient;
import com.warehouse.redirect.domain.model.Sender;

import lombok.Value;

@Value
public class RedirectParcel {

    Sender sender;

    Recipient recipient;

    Size parcelSize;

    ParcelStatus status;

    ParcelType parcelType;

    Long parcelRelatedId;

    String destination;

    public boolean isParent() {
        return parcelType.equals(ParcelType.PARENT);
    }

    public boolean hasStatusCreated() {
        return status.equals(ParcelStatus.CREATED);
    }

    public boolean isRequiredToRedirect() {
        return hasStatusCreated() && isParent();
    }
}
