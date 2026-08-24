package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.shipment.domain.vo.conf.OperatorShipmentConfiguration;
import com.warehouse.shipment.domain.vo.conf.ShipmentLabelFormat;
import com.warehouse.shipment.domain.vo.conf.ShipmentLabelSettings;
import com.warehouse.shipment.domain.vo.conf.ShipmentLimits;
import com.warehouse.shipment.domain.vo.conf.ShipmentNotificationChannel;
import com.warehouse.shipment.domain.vo.conf.ShipmentNotificationSettings;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.domain.vo.conf.ShipmentValidationRules;
import com.warehouse.shipment.domain.vo.conf.ShipmentWorkflowSettings;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberDateFormat;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberRule;
import com.warehouse.shipment.domain.vo.conf.TrackingNumberSource;
import org.junit.jupiter.api.Test;

class OperatorShipmentConfigurationTest {

    @Test
    void shouldUseDefaultSectionsWhenConfigurationIsIncomplete() {
        final OperatorShipmentConfiguration configuration = new OperatorShipmentConfiguration(
                null,
                new ShipmentLabelSettings(true, true, true, null),
                null,
                new ShipmentWorkflowSettings(ShipmentStatus.ACCEPTED, null, true, false, true, 60, ""),
                new TrackingNumberRule("", null, null, 10, false, null, false),
                new ShipmentNotificationSettings(false, false, false, false, null)
        );

        assertThat(configuration.validationRules()).isEqualTo(ShipmentValidationRules.defaults());
        assertThat(configuration.limits()).isEqualTo(ShipmentLimits.defaults());
        assertThat(configuration.labelSettings().labelFormat()).isEqualTo(ShipmentLabelFormat.PDF_A6);
        assertThat(configuration.workflowSettings().defaultStatus()).isEqualTo(ShipmentStatus.ACCEPTED);
        assertThat(configuration.workflowSettings().defaultServiceLevel()).isEqualTo(ShipmentServiceLevel.STANDARD);
        assertThat(configuration.workflowSettings().pickupCutoffTime()).isEqualTo("16:00");
        assertThat(configuration.trackingNumberRule().key()).isEqualTo("MGR");
        assertThat(configuration.trackingNumberRule().separator()).isEqualTo("-");
        assertThat(configuration.trackingNumberRule().source()).isEqualTo(TrackingNumberSource.SEQUENCE);
        assertThat(configuration.trackingNumberRule().dateFormat()).isEqualTo(TrackingNumberDateFormat.YYYYMMDD);
        assertThat(configuration.notificationSettings().notificationChannel()).isEqualTo(ShipmentNotificationChannel.SMS);
    }

    @Test
    void shouldExposeDefaultsForOperatorShipmentConfiguration() {
        final OperatorShipmentConfiguration defaults = OperatorShipmentConfiguration.defaults();

        assertThat(defaults.workflowSettings().defaultStatus()).isEqualTo(ShipmentStatus.CREATED);
        assertThat(defaults.workflowSettings().defaultServiceLevel()).isEqualTo(ShipmentServiceLevel.STANDARD);
        assertThat(defaults.labelSettings().labelFormat()).isEqualTo(ShipmentLabelFormat.PDF_A6);
        assertThat(defaults.trackingNumberRule().source()).isEqualTo(TrackingNumberSource.SEQUENCE);
        assertThat(defaults.notificationSettings().notificationChannel()).isEqualTo(ShipmentNotificationChannel.SMS);
        assertThat(defaults.limits().maxWeight()).isEqualTo(31.5);
    }
}
