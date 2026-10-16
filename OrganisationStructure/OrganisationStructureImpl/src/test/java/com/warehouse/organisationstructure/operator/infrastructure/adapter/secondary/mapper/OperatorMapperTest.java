package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.dto.DeliveryAreaModeDto;
import com.warehouse.organisationstructure.api.dto.DepartmentCapacityPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConnectionPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentDeliveryAreaPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentLifecyclePolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentWorkTransferModeDto;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.api.dto.DefaultPickupPointTypeDto;
import com.warehouse.organisationstructure.api.dto.PickupPointConfigurationDto;
import com.warehouse.organisationstructure.api.dto.RecipientVerificationMethodDto;
import com.warehouse.organisationstructure.api.dto.UncoveredPostalCodePolicyDto;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DeliveryAreaMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentCapacityPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentWorkTransferMode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultPickupPointType;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FullPickupPointPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OversizedLockerShipmentPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointDefaultsPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLifecyclePolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointLockerPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointMaxShipmentSize;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointPreference;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointSelectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SuspendedPickupPointShipmentPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UncoveredPostalCodePolicy;

class OperatorMapperTest {

    @Test
    void shouldMapOperatorToEntity() {
        final OperatorEntity entity = OperatorMapper.toEntity(OperatorTestFixtures.operator());

        assertEquals(OperatorTestFixtures.OPERATOR_ID, entity.getOperatorId());
        assertEquals(OperatorTestFixtures.REGISTERING_USER_ID, entity.getRegisteringUserId());
        assertEquals(OperatorTestFixtures.TAX_ID, entity.getTaxId());
        assertEquals("Example Logistics", entity.getCompanyName());
        assertEquals(OperatorTestFixtures.CREATED_AT, entity.getCreatedAt());
    }

    @Test
    void shouldMapEntityToOperatorWithDefaultConfiguration() {
        final OperatorEntity entity = OperatorMapper.toEntity(OperatorTestFixtures.operator());

        final Operator operator = OperatorMapper.toModel(entity);

        assertEquals(OperatorTestFixtures.OPERATOR_ID, operator.getOperatorId());
        assertEquals("Example Logistics", operator.getCompanyName());
        assertNotNull(operator.getConfiguration());
        assertEquals(entity.getStatus(), operator.getStatus());
    }

    @Test
    void shouldMapOperatorToDtoWithConfiguration() {
        final OperatorDto dto = OperatorMapper.toDto(OperatorTestFixtures.operator());

        assertEquals(OperatorTestFixtures.OPERATOR_ID, dto.operatorId());
        assertEquals(OperatorStatusDto.ACTIVE, dto.status());
        assertNotNull(dto.configuration());
        assertEquals(31.5, dto.configuration().shipmentConfiguration().shipmentLimits().maxWeight());
        assertEquals(8, dto.configuration().deliveryTimeConfiguration().sameDayDeliveryHours());
    }

    @Test
    void shouldMapConfigurationDtoToModel() {
        final OperatorConfiguration configuration =
                OperatorMapper.toModelConfiguration(OperatorTestFixtures.configurationDto());

        assertTrueConfiguration(configuration);
    }

    @Test
    void shouldMapOperatorIdToDto() {
        final OperatorIdDto dto = OperatorMapper.toDtoId(OperatorTestFixtures.OPERATOR_ID);

        assertEquals(OperatorTestFixtures.OPERATOR_ID.value(), dto.value());
    }

    @Test
    void shouldReturnNullForNullInputs() {
        assertNull(OperatorMapper.toModel(null));
        assertNull(OperatorMapper.toDto(null));
        assertNull(OperatorMapper.toModelConfiguration(null));
    }

    private void assertTrueConfiguration(final OperatorConfiguration configuration) {
        assertNotNull(configuration);
        assertEquals(31.5, configuration.getShipmentLimits().getMaxWeight());
        assertEquals(4, configuration.getDeliveryTimeConfiguration().getMaxDeliveryDays());
        assertEquals(true, configuration.getShippingCapabilities().isSupportsExpressShipping());
    }

    @Test
    void shouldMapDepartmentPoliciesBothWays() {
        final DepartmentConfigurationDto dto = new DepartmentConfigurationDto(true, false, true, false,
                new DepartmentDeliveryAreaPolicyDto(DeliveryAreaModeDto.RADIUS, 40, false, true,
                        UncoveredPostalCodePolicyDto.ASSIGN_NEAREST),
                new DepartmentConnectionPolicyDto(2, 250, true, false),
                new DepartmentLifecyclePolicyDto(true, true, DepartmentWorkTransferModeDto.AUTOMATIC, 30),
                new DepartmentCapacityPolicyDto(1500, 90, true, 25));

        final DepartmentConfiguration configuration = OperatorMapper.toModelDepartmentConfiguration(dto);

        assertEquals(DeliveryAreaMode.RADIUS, configuration.getDeliveryAreaPolicy().getDeliveryAreaMode());
        assertEquals(2, configuration.getConnectionPolicy().getSortingCenterConnectionCount());
        assertEquals(DepartmentWorkTransferMode.AUTOMATIC,
                configuration.getLifecyclePolicy().getWorkTransferOnDeactivation());
        assertEquals(dto, OperatorMapper.toDtoDepartmentConfiguration(configuration));
    }

    @Test
    void shouldUseDefaultDepartmentPoliciesWhenRequestOmitsThem() {
        final DepartmentConfiguration configuration = OperatorMapper.toModelDepartmentConfiguration(
                new DepartmentConfigurationDto(false, true, false, false));

        assertEquals(DepartmentCapacityPolicy.defaultConfiguration().getDefaultDailyCapacity(),
                configuration.getCapacityPolicy().getDefaultDailyCapacity());
        assertEquals(UncoveredPostalCodePolicy.WARN,
                configuration.getDeliveryAreaPolicy().getUncoveredPostalCodePolicy());
    }

    @Test
    void shouldMapPickupPointConfigurationBothWays() {
        final PickupPointConfiguration configuration = new PickupPointConfiguration(
                new PickupPointDefaultsPolicy(DefaultPickupPointType.PARCEL_LOCKER, false, true,
                        PickupPointMaxShipmentSize.SMALL, PickupPointMaxShipmentSize.BIG, true, "07:00", "21:00",
                        false, "10:00", "13:00", false),
                null,
                new PickupPointLockerPolicy(30, 12, OversizedLockerShipmentPolicy.RETURN_TO_DEPARTMENT, 5),
                new PickupPointSelectionPolicy(10, PickupPointPreference.PARCEL_LOCKER, 5,
                        FullPickupPointPolicy.REJECT, true),
                null,
                new PickupPointLifecyclePolicy(false, SuspendedPickupPointShipmentPolicy.WAIT, false, 14));

        final PickupPointConfigurationDto dto = OperatorMapper.toDtoPickupPointConfiguration(configuration);
        final PickupPointConfiguration mappedBack = OperatorMapper.toModelPickupPointConfiguration(dto);

        assertEquals(DefaultPickupPointTypeDto.PARCEL_LOCKER, dto.defaultsPolicy().defaultType());
        assertEquals(RecipientVerificationMethodDto.PICKUP_CODE, dto.collectionPolicy().verificationMethod());
        assertEquals(dto, OperatorMapper.toDtoPickupPointConfiguration(mappedBack));
        assertEquals(SuspendedPickupPointShipmentPolicy.WAIT,
                mappedBack.getLifecyclePolicy().getSuspendedPointShipmentPolicy());
    }
}
