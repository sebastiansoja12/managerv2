package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LabelFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.TrackingNumberDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;

class OperatorConfigurationMapperTest {

    @Test
    void shouldMapConfigurationToEntityAndBack() {
        final OperatorConfigurationEntity entity = OperatorConfigurationMapper.toEntity(
                OperatorTestFixtures.OPERATOR_ID,
                OperatorTestFixtures.configuration()
        );

        final OperatorConfiguration configuration = OperatorConfigurationMapper.toModel(entity);

        assertNotNull(configuration);
        assertTrue(configuration.getShippingCapabilities().isSupportsInternationalShipping());
        assertTrue(configuration.getShippingCapabilities().isProvidesInsurance());
        assertEquals(31.5, configuration.getShipmentLimits().getMaxWeight());
        assertFalse(configuration.getShipmentLimits().isAllowOversized());
        assertTrue(configuration.getShipmentConfiguration().getValidationConfiguration().isRequireRecipientPhone());
        assertEquals(LabelFormat.PDF_A6,
                configuration.getShipmentConfiguration().getLabelConfiguration().getLabelFormat());
        assertEquals(7, configuration.getDeliveryTimeConfiguration().getInternationalDeliveryDays());
    }

    @Test
    void shouldFallbackToDefaultConfigurationWhenSourceIsNull() {
        final OperatorConfigurationEntity entity = OperatorConfigurationMapper.toEntity(
                OperatorTestFixtures.OPERATOR_ID,
                null
        );

        final OperatorConfiguration configuration = OperatorConfigurationMapper.toModel(entity);

        assertTrue(configuration.getShippingCapabilities().isSupportsDomesticShipping());
        assertFalse(configuration.getShippingCapabilities().isSupportsInternationalShipping());
        assertFalse(configuration.getShippingCapabilities().isSupportsCashOnDelivery());
        assertEquals(TrackingNumberDateFormat.YYYYMMDD,
                configuration.getShipmentConfiguration().getTrackingNumberRule().getDateFormat());
    }

    @Test
    void shouldReturnNullForNullEntity() {
        assertNull(OperatorConfigurationMapper.toModel(null));
    }
}
