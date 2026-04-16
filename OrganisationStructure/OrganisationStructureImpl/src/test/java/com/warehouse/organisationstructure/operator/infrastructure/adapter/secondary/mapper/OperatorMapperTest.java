package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.dto.OperatorDto;
import com.warehouse.organisationstructure.api.dto.OperatorIdDto;
import com.warehouse.organisationstructure.api.dto.OperatorStatusDto;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

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
        assertEquals(31.5, dto.configuration().shipmentLimits().maxWeight());
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
}
