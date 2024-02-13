package com.warehouse.redirect.domain.model;


import com.warehouse.redirect.domain.enumeration.ParcelType;
import com.warehouse.redirect.domain.enumeration.Size;
import com.warehouse.redirect.domain.enumeration.Status;
import lombok.Data;

@Data
public class RedirectParcelResponse {
    private Long parcelId;
    private Sender sender;
    private Recipient recipient;
    private Size parcelSize;
    private Status status;
    private ParcelType parcelType;
    private Long parcelRelatedId;
}
