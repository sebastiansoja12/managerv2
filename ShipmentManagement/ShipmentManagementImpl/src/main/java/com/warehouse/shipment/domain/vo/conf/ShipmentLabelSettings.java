package com.warehouse.shipment.domain.vo.conf;

import java.util.Objects;

public record ShipmentLabelSettings(
        boolean autoGenerateLabels,
        boolean includeReturnLabel,
        boolean attachPackingSlip,
        ShipmentLabelFormat labelFormat
) {

    public ShipmentLabelSettings {
        labelFormat = Objects.requireNonNullElse(labelFormat, ShipmentLabelFormat.PDF_A6);
    }

    public static ShipmentLabelSettings defaults() {
        return new ShipmentLabelSettings(false, false, false, ShipmentLabelFormat.PDF_A6);
    }
}
