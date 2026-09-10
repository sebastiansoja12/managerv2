package com.warehouse.organisationstructure.api.dto;

public record ShipmentLabelConfigurationDto(
        boolean autoGenerateLabels,
        boolean includeReturnLabel,
        boolean attachPackingSlip,
        ShipmentLabelFormatDto labelFormat
) {
}
