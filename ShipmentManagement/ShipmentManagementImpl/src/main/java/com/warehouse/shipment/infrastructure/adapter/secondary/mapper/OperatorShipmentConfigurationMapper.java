package com.warehouse.shipment.infrastructure.adapter.secondary.mapper;

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

public class OperatorShipmentConfigurationMapper {

    public OperatorShipmentConfiguration map(final ShipmentConfigurationDto configuration) {
        if (configuration == null) {
            return OperatorShipmentConfiguration.defaults();
        }

        return new OperatorShipmentConfiguration(
                map(configuration.validationConfiguration()),
                map(configuration.labelConfiguration()),
                map(configuration.shipmentLimits()),
                map(configuration.workflowConfiguration()),
                map(configuration.trackingNumberRule()),
                map(configuration.notificationConfiguration())
        );
    }

    private ShipmentValidationRules map(final ShipmentValidationConfigurationDto configuration) {
        if (configuration == null) {
            return ShipmentValidationRules.defaults();
        }

        return new ShipmentValidationRules(
                configuration.validateAddressData(),
                configuration.requireRecipientPhone(),
                configuration.requireRecipientEmail(),
                configuration.preventDuplicateTracking(),
                configuration.requireSenderReference(),
                configuration.validatePostalCode()
        );
    }

    private ShipmentLabelSettings map(final ShipmentLabelConfigurationDto configuration) {
        if (configuration == null) {
            return ShipmentLabelSettings.defaults();
        }

        return new ShipmentLabelSettings(
                configuration.autoGenerateLabels(),
                configuration.includeReturnLabel(),
                configuration.attachPackingSlip(),
                map(configuration.labelFormat())
        );
    }

    private ShipmentLimits map(final ShipmentLimitsDto limits) {
        if (limits == null) {
            return ShipmentLimits.defaults();
        }

        return new ShipmentLimits(
                limits.maxWeight(),
                limits.minWeight(),
                limits.maxLength(),
                limits.maxWidth(),
                limits.maxHeight(),
                limits.maxShipmentValue(),
                limits.allowOversized()
        );
    }

    private ShipmentWorkflowSettings map(final ShipmentWorkflowConfigurationDto configuration) {
        if (configuration == null) {
            return ShipmentWorkflowSettings.defaults();
        }

        return new ShipmentWorkflowSettings(
                map(configuration.defaultStatus()),
                map(configuration.defaultServiceLevel()),
                configuration.autoAssignCourier(),
                configuration.autoCloseDelivered(),
                configuration.generateTrackingNumber(),
                configuration.cancellationWindowMinutes(),
                configuration.pickupCutoffTime()
        );
    }

    private TrackingNumberRule map(final TrackingNumberRuleDto rule) {
        if (rule == null) {
            return TrackingNumberRule.defaults();
        }

        return new TrackingNumberRule(
                rule.key(),
                rule.separator(),
                map(rule.source()),
                rule.randomLength(),
                rule.includeDate(),
                map(rule.dateFormat()),
                rule.uppercase()
        );
    }

    private ShipmentNotificationSettings map(final ShipmentNotificationConfigurationDto configuration) {
        if (configuration == null) {
            return ShipmentNotificationSettings.defaults();
        }

        return new ShipmentNotificationSettings(
                configuration.notifyRecipientOnCreated(),
                configuration.notifyRecipientOnDispatched(),
                configuration.notifyRecipientOnDelivered(),
                configuration.notifySenderOnException(),
                map(configuration.notificationChannel())
        );
    }

    private ShipmentStatus map(final DefaultShipmentStatusDto status) {
        return status == null ? null : ShipmentStatus.valueOf(status.name());
    }

    private ShipmentServiceLevel map(final ShipmentServiceLevelDto serviceLevel) {
        return serviceLevel == null ? null : ShipmentServiceLevel.valueOf(serviceLevel.name());
    }

    private ShipmentLabelFormat map(final ShipmentLabelFormatDto format) {
        return format == null ? null : ShipmentLabelFormat.valueOf(format.name());
    }

    private TrackingNumberSource map(final TrackingNumberSourceDto source) {
        return source == null ? null : TrackingNumberSource.valueOf(source.name());
    }

    private TrackingNumberDateFormat map(final TrackingNumberDateFormatDto dateFormat) {
        return dateFormat == null ? null : TrackingNumberDateFormat.valueOf(dateFormat.name());
    }

    private ShipmentNotificationChannel map(final ShipmentNotificationChannelDto channel) {
        return channel == null ? null : ShipmentNotificationChannel.valueOf(channel.name());
    }
}
