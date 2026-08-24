package com.warehouse.organisationstructure;

import java.time.Instant;
import java.time.LocalDate;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.api.dto.CreateOperatorApiRequest;
import com.warehouse.organisationstructure.api.dto.DefaultShipmentStatusDto;
import com.warehouse.organisationstructure.api.dto.DeliveryTimeConfigurationDto;
import com.warehouse.organisationstructure.api.dto.FirstDepartmentDto;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorGeocodingConfigurationDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.api.dto.ShipmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLabelFormatDto;
import com.warehouse.organisationstructure.api.dto.ShipmentLimitsDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationChannelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentNotificationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentServiceLevelDto;
import com.warehouse.organisationstructure.api.dto.ShipmentValidationConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShipmentWorkflowConfigurationDto;
import com.warehouse.organisationstructure.api.dto.ShippingCapabilitiesDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberDateFormatDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberRuleDto;
import com.warehouse.organisationstructure.api.dto.TrackingNumberSourceDto;
import com.warehouse.organisationstructure.api.dto.UpdateOperatorApiRequest;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorProvisioningDetails;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorGeocodingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultShipmentStatus;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryTimeConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LabelFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.NotificationChannel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ServiceLevel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLabelConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentWorkflowConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShippingCapabilities;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberRule;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberSource;

public final class OperatorTestFixtures {

    public static final OperatorId OPERATOR_ID = OperatorId.of(10001L);
    public static final UserId REGISTERING_USER_ID = new UserId(2100L);
    public static final TaxId TAX_ID = TaxId.of("5250001122");
    public static final Instant CREATED_AT = Instant.parse("2026-07-18T10:15:30Z");
    public static final Instant UPDATED_AT = Instant.parse("2026-07-18T11:15:30Z");
    public static final LocalDate CONTRACT_START = LocalDate.of(2026, 1, 1);
    public static final LocalDate CONTRACT_END = LocalDate.of(2027, 1, 1);
    public static final LocalDate FOUNDED_DATE = LocalDate.of(2020, 5, 12);

    private OperatorTestFixtures() {
    }

    public static Operator operator() {
        return operator(OPERATOR_ID);
    }

    public static Operator operator(final OperatorId operatorId) {
        return new Operator(
                operatorId,
                REGISTERING_USER_ID,
                TAX_ID,
                true,
                true,
                false,
                "+48123123123",
                "ops@example.com",
                "Example Logistics",
                CONTRACT_START,
                CONTRACT_END,
                FOUNDED_DATE,
                configuration(),
                provisioningDetails(),
                OperatorStatus.ACTIVE,
                CREATED_AT,
                UPDATED_AT
        );
    }

    public static OperatorConfiguration configuration() {
        return new OperatorConfiguration(
                new ShippingCapabilities(
                        true,
                        true,
                        true,
                        false,
                        true,
                        true,
                        false,
                        true,
                        true,
                        false,
                        true,
                        true,
                        true
                ),
                shipmentConfiguration(),
                new DeliveryTimeConfiguration(
                        1,
                        4,
                        1,
                        8,
                        7
                )
        );
    }

    public static OperatorConfiguration updatedConfiguration() {
        return new OperatorConfiguration(
                new ShippingCapabilities(
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        true,
                        true,
                        false,
                        false,
                        true,
                        true,
                        false
                ),
                updatedShipmentConfiguration(),
                new DeliveryTimeConfiguration(
                        2,
                        5,
                        2,
                        0,
                        10
                )
        );
    }

    public static ShipmentConfiguration shipmentConfiguration() {
        return new ShipmentConfiguration(
                new ShipmentValidationConfiguration(
                        true,
                        true,
                        false,
                        true,
                        false,
                        true
                ),
                new ShipmentLabelConfiguration(
                        false,
                        false,
                        false,
                        LabelFormat.PDF_A6
                ),
                new ShipmentLimits(
                        31.5,
                        0.2,
                        120.0,
                        80.0,
                        60.0,
                        5000.0,
                        false
                ),
                new ShipmentWorkflowConfiguration(
                        DefaultShipmentStatus.CREATED,
                        ServiceLevel.STANDARD,
                        false,
                        true,
                        false,
                        30,
                        "16:00"
                ),
                new TrackingNumberRule(
                        "MGR",
                        "-",
                        TrackingNumberSource.SEQUENCE,
                        8,
                        true,
                        TrackingNumberDateFormat.YYYYMMDD,
                        true
                ),
                new ShipmentNotificationConfiguration(
                        true,
                        true,
                        true,
                        true,
                        NotificationChannel.SMS
                )
        );
    }

    public static ShipmentConfiguration updatedShipmentConfiguration() {
        return new ShipmentConfiguration(
                new ShipmentValidationConfiguration(
                        false,
                        true,
                        true,
                        false,
                        true,
                        false
                ),
                new ShipmentLabelConfiguration(
                        true,
                        true,
                        true,
                        LabelFormat.ZPL
                ),
                new ShipmentLimits(
                        20.0,
                        1.0,
                        90.0,
                        60.0,
                        40.0,
                        2500.0,
                        true
                ),
                new ShipmentWorkflowConfiguration(
                        DefaultShipmentStatus.ACCEPTED,
                        ServiceLevel.EXPRESS,
                        true,
                        false,
                        true,
                        45,
                        "15:00"
                ),
                new TrackingNumberRule(
                        "CLIENT",
                        "/",
                        TrackingNumberSource.SHIPMENT_ID,
                        10,
                        false,
                        TrackingNumberDateFormat.YYMMDD,
                        false
                ),
                new ShipmentNotificationConfiguration(
                        true,
                        false,
                        true,
                        false,
                        NotificationChannel.BOTH
                )
        );
    }

    public static OperatorProvisioningDetails provisioningDetails() {
        return new OperatorProvisioningDetails(
                "Jan",
                "Kowalski",
                "j.kowalski",
                "secret",
                "pl",
                "jan.kowalski@example.com",
                geocodingConfiguration(),
                new OperatorProvisioningDetails.FirstDepartment(
                        "WRO-1",
                        "Wroclaw",
                        "Robotnicza 1",
                        "50-001",
                        "PL",
                        "8-16",
                        "WAREHOUSE"
                )
        );
    }

    public static OperatorGeocodingConfiguration geocodingConfiguration() {
        return new OperatorGeocodingConfiguration(
                null,
                null,
                "position-stack-api-key",
                null,
                null,
                null,
                true,
                GeocodingProvider.POSITION_STACK
        );
    }

    public static CreateOperatorCommand createCommand() {
        final OperatorProvisioningDetails details = provisioningDetails();
        final OperatorProvisioningDetails.FirstDepartment department = details.firstDepartment();
        return new CreateOperatorCommand(
                details.userFirstName(),
                details.userLastName(),
                details.username(),
                details.password(),
                details.language(),
                details.email(),
                TAX_ID,
                true,
                true,
                false,
                "+48123123123",
                "ops@example.com",
                "Example Logistics",
                CONTRACT_START,
                CONTRACT_END,
                FOUNDED_DATE,
                configuration(),
                details.geocodingConfiguration(),
                new CreateOperatorCommand.FirstDepartment(
                        department.departmentCode(),
                        department.city(),
                        department.street(),
                        department.postalCode(),
                        department.countryCode(),
                        department.openingHours(),
                        department.departmentType()
                )
        );
    }

    public static UpdateOperatorCommand updateCommand() {
        return new UpdateOperatorCommand(
                TaxId.of("5259998877"),
                false,
                false,
                false,
                "+48999111222",
                "support@example.com",
                "Updated Logistics",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2028, 2, 1),
                LocalDate.of(2019, 2, 2),
                updatedConfiguration(),
                OperatorStatus.SUSPENDED
        );
    }

    public static OperatorConfigurationDto configurationDto() {
        return new OperatorConfigurationDto(
                new ShippingCapabilitiesDto(
                        true,
                        true,
                        true,
                        false,
                        true,
                        true,
                        false,
                        true,
                        true,
                        false,
                        true,
                        true,
                        true
                ),
                shipmentConfigurationDto(),
                new DeliveryTimeConfigurationDto(
                        1,
                        4,
                        1,
                        8,
                        7
                )
        );
    }

    public static ShipmentConfigurationDto shipmentConfigurationDto() {
        return new ShipmentConfigurationDto(
                new ShipmentValidationConfigurationDto(
                        true,
                        true,
                        false,
                        true,
                        false,
                        true
                ),
                new ShipmentLabelConfigurationDto(
                        false,
                        false,
                        false,
                        ShipmentLabelFormatDto.PDF_A6
                ),
                new ShipmentLimitsDto(
                        31.5,
                        0.2,
                        120.0,
                        80.0,
                        60.0,
                        5000.0,
                        false
                ),
                new ShipmentWorkflowConfigurationDto(
                        DefaultShipmentStatusDto.CREATED,
                        ShipmentServiceLevelDto.STANDARD,
                        false,
                        true,
                        false,
                        30,
                        "16:00"
                ),
                new TrackingNumberRuleDto(
                        "MGR",
                        "-",
                        TrackingNumberSourceDto.SEQUENCE,
                        8,
                        true,
                        TrackingNumberDateFormatDto.YYYYMMDD,
                        true
                ),
                new ShipmentNotificationConfigurationDto(
                        true,
                        true,
                        true,
                        true,
                        ShipmentNotificationChannelDto.SMS
                )
        );
    }

    public static CreateOperatorApiRequest createApiRequest() {
        final OperatorProvisioningDetails details = provisioningDetails();
        final OperatorProvisioningDetails.FirstDepartment department = details.firstDepartment();
        return new CreateOperatorApiRequest(
                details.userFirstName(),
                details.userLastName(),
                details.username(),
                details.password(),
                details.language(),
                details.email(),
                TAX_ID.value(),
                true,
                true,
                false,
                "+48123123123",
                "ops@example.com",
                "Example Logistics",
                CONTRACT_START,
                CONTRACT_END,
                FOUNDED_DATE,
                configurationDto(),
                new OperatorGeocodingConfigurationDto(
                        details.geocodingConfiguration().apiUserName(),
                        details.geocodingConfiguration().apiPassword(),
                        details.geocodingConfiguration().apiKey(),
                        details.geocodingConfiguration().clientNumber(),
                        details.geocodingConfiguration().accessToken(),
                        details.geocodingConfiguration().refreshToken(),
                        details.geocodingConfiguration().enabled(),
                        details.geocodingConfiguration().provider()
                ),
                new FirstDepartmentDto(
                        department.departmentCode(),
                        department.city(),
                        department.street(),
                        department.postalCode(),
                        department.countryCode(),
                        department.openingHours(),
                        department.departmentType()
                )
        );
    }

    public static UpdateOperatorApiRequest updateApiRequest() {
        return new UpdateOperatorApiRequest(
                "5259998877",
                false,
                false,
                false,
                "+48999111222",
                "support@example.com",
                "Updated Logistics",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2028, 2, 1),
                LocalDate.of(2019, 2, 2),
                configurationDto(),
                OperatorStatusDto.SUSPENDED
        );
    }
}
