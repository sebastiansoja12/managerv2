package com.warehouse.redirect.domain.model;


import com.warehouse.redirect.domain.enumeration.ParcelType;
import com.warehouse.redirect.domain.enumeration.Size;
import com.warehouse.redirect.domain.enumeration.Status;

public class RedirectParcel {
    Sender sender;

    Recipient recipient;

    Size parcelSize;

    Status status;

    ParcelType parcelType;

    Long parcelRelatedId;

    String destination;

    public boolean isParent() {
        return parcelType.equals(ParcelType.PARENT);
    }

    public boolean hasStatusCreated() {
        return status.equals(Status.CREATED);
    }

    public boolean hasStatusReroute() {
        return status.equals(Status.REROUTE);
    }

    public boolean isRequiredToRedirect() {
        return hasStatusCreated() && isParent();
    }
}
