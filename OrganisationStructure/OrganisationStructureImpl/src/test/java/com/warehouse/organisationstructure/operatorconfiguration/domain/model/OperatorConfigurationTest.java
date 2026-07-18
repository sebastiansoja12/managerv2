package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.warehouse.organisationstructure.OperatorTestFixtures;

class OperatorConfigurationTest {

    @Test
    void shouldBuildDefaultConfigurationFromOperatorFlags() {
        final OperatorConfiguration configuration = OperatorConfiguration.defaultFor(true, false, true);

        final OperatorConfiguration.ShippingCapabilities capabilities =
                configuration.getShippingCapabilities();

        assertTrue(capabilities.isSupportsDomesticShipping());
        assertTrue(capabilities.isSupportsInternationalShipping());
        assertFalse(capabilities.isSupportsCashOnDelivery());
        assertTrue(capabilities.isSupportsParcelLockers());
        assertTrue(capabilities.isSupportsHomeDelivery());
        assertTrue(capabilities.isProvidesTracking());
        assertNotNull(configuration.getShipmentLimits());
        assertNotNull(configuration.getDeliveryTimeConfiguration());
    }

    @Test
    void shouldKeepExplicitLimitsAndDeliveryTimes() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration();

        assertEquals(31.5, configuration.getShipmentLimits().getMaxWeight());
        assertEquals(0.2, configuration.getShipmentLimits().getMinWeight());
        assertEquals(120.0, configuration.getShipmentLimits().getMaxLength());
        assertEquals(4, configuration.getDeliveryTimeConfiguration().getMaxDeliveryDays());
        assertEquals(8, configuration.getDeliveryTimeConfiguration().getSameDayDeliveryHours());
    }
}
