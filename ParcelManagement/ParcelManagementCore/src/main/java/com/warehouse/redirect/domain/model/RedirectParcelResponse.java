package com.warehouse.redirect.domain.model;


import com.warehouse.redirect.domain.enumeration.ParcelType;
import com.warehouse.redirect.domain.enumeration.Size;
import com.warehouse.redirect.domain.enumeration.Status;
import lombok.Data;

@Data
public class RedirectParcelResponse {
    Long parcelId;
    Sender sender;
    Recipient recipient;
    Size parcelSize;
    Status status;
    ParcelType parcelType;
    Long parcelRelatedId;
}
