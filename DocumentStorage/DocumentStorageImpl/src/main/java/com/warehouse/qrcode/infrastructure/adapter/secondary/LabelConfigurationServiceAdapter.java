package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelConfigurationDto;
import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.port.secondary.LabelConfigurationServicePort;

public class LabelConfigurationServiceAdapter implements LabelConfigurationServicePort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public LabelConfigurationServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public LabelFormat currentLabelFormat() {
        final ShipmentConfigurationDto configuration = operatorConfigurationApiService.getCurrentShipmentConfiguration();
        if (configuration == null) {
            return LabelFormat.PDF_A6;
        }
        final ShipmentLabelConfigurationDto labelConfiguration = configuration.labelConfiguration();
        if (labelConfiguration == null || labelConfiguration.labelFormat() == null) {
            return LabelFormat.PDF_A6;
        }
        return LabelFormat.valueOf(labelConfiguration.labelFormat().name());
    }
}
