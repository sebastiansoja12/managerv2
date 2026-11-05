package com.warehouse.qrcode.domain.model;

import java.math.BigDecimal;

import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.qrcode.domain.vo.LabelParty;
import com.warehouse.qrcode.domain.vo.ParcelDimensions;
import com.warehouse.qrcode.domain.vo.ShipmentId;

public record Shipment(ShipmentId shipmentId,
                       TrackingNumber trackingNumber,
                       LabelParty sender,
                       LabelParty recipient,
                       BigDecimal weightInKilograms,
                       ParcelDimensions dimensions,
                       ShipmentPriority priority,
                       String serviceLevel) {

    public String trackingNumberValue() {
        return trackingNumber == null ? null : trackingNumber.value();
    }

    public String formattedWeight() {
        return weightInKilograms == null ? null : weightInKilograms.stripTrailingZeros().toPlainString() + " kg";
    }

    public String formattedDimensions() {
        return dimensions == null ? null : dimensions.format();
    }
}
