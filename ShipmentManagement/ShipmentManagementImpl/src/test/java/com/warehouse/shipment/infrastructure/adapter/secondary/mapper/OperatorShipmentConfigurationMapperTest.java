package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.organisationstructure.api.dto.DefaultShipmentStatusDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelFormatDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationChannelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentServiceLevelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentValidationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentWorkflowConfigurationDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberDateFormatDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberRuleDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberSourceDto;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLabelFormat;
import com.warehouse.shipment.domain.vo.conf.ShipmentNotificationChannel;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberSource;
import org.junit.jupiter.api.Test;

class OperatorShipmentConfigurationMapperTest {

    private final OperatorShipmentConfigurationMapper mapper = new OperatorShipmentConfigurationMapper();

    @Test
    void shouldMapShipmentConfigurationDtoToDomainVo() {
        final ShipmentConfigurationDto dto = new ShipmentConfigurationDto(
                new ShipmentValidationConfigurationDto(false, true, true, false, true, false),
                new ShipmentLabelConfigurationDto(true, true, true, ShipmentLabelFormatDto.ZPL),
                new ShipmentLimitsDto(50.0, 1.0, 200.0, 100.0, 90.0, 5000.0, true),
                new ShipmentWorkflowConfigurationDto(
                        DefaultShipmentStatusDto.ACCEPTED,
                        ShipmentServiceLevelDto.EXPRESS,
                        true,
                        false,
                        true,
                        90,
                        "18:30"
                ),
                new TrackingNumberRuleDto(
                        "FTM",
                        "/",
                        TrackingNumberSourceDto.RANDOM,
                        12,
                        false,
                        TrackingNumberDateFormatDto.YYMMDD,
                        false
                ),
                new ShipmentNotificationConfigurationDto(false, true, false, true, ShipmentNotificationChannelDto.BOTH)
        );

        final OperatorShipmentConfiguration configuration = mapper.map(dto);

        assertThat(configuration.validationRules().validateAddressData()).isFalse();
        assertThat(configuration.validationRules().requireRecipientPhone()).isTrue();
        assertThat(configuration.labelSettings().labelFormat()).isEqualTo(ShipmentLabelFormat.ZPL);
        assertThat(configuration.labelSettings().autoGenerateLabels()).isTrue();
        assertThat(configuration.limits().maxWeight()).isEqualTo(50.0);
        assertThat(configuration.limits().allowOversized()).isTrue();
        assertThat(configuration.workflowSettings().defaultStatus()).isEqualTo(ShipmentStatus.ACCEPTED);
        assertThat(configuration.workflowSettings().defaultServiceLevel()).isEqualTo(ShipmentServiceLevel.EXPRESS);
        assertThat(configuration.workflowSettings().pickupCutoffTime()).isEqualTo("18:30");
        assertThat(configuration.trackingNumberRule().key()).isEqualTo("FTM");
        assertThat(configuration.trackingNumberRule().source()).isEqualTo(TrackingNumberSource.RANDOM);
        assertThat(configuration.trackingNumberRule().dateFormat()).isEqualTo(TrackingNumberDateFormat.YYMMDD);
        assertThat(configuration.notificationSettings().notificationChannel()).isEqualTo(ShipmentNotificationChannel.BOTH);
    }

    @Test
    void shouldReturnDefaultsWhenDtoIsNull() {
        final OperatorShipmentConfiguration configuration = mapper.map(null);

        assertThat(configuration).isEqualTo(OperatorShipmentConfiguration.defaults());
    }
}
