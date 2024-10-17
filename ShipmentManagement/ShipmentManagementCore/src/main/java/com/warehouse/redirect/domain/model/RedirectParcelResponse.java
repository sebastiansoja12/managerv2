package com.warehouse.redirect.domain.model;



import com.warehouse.commonassets.enumeration.ParcelStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.enumeration.Size;
import lombok.Data;

@Data
public class RedirectParcelResponse {
    private Long parcelId;
    private Sender sender;
    private Recipient recipient;
    private Size parcelSize;
    private ParcelStatus status;
    private ShipmentType shipmentType;
    private Long parcelRelatedId;
}
