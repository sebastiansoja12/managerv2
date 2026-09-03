package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OperatorConfigurationServiceAdapterTest {

    @Test
    void shouldAllowReturnsWhenCurrentOperatorSupportsThem() {
        final OperatorConfigurationPort operatorConfigurationPort = mock(OperatorConfigurationPort.class);
        when(operatorConfigurationPort.getCurrent()).thenReturn(Optional.of(OperatorTestFixtures.configuration()));
        final OperatorConfigurationServiceAdapter adapter = new OperatorConfigurationServiceAdapter(operatorConfigurationPort);

        final boolean supportsReturns = adapter.supportsReturnShipments();

        assertTrue(supportsReturns);
    }

    @Test
    void shouldDenyReturnsWhenCurrentOperatorDoesNotSupportThem() {
        final OperatorConfigurationPort operatorConfigurationPort = mock(OperatorConfigurationPort.class);
        when(operatorConfigurationPort.getCurrent()).thenReturn(Optional.of(OperatorConfiguration.defaultFor(false, false, false)));
        final OperatorConfigurationServiceAdapter adapter = new OperatorConfigurationServiceAdapter(operatorConfigurationPort);

        final boolean supportsReturns = adapter.supportsReturnShipments();

        assertFalse(supportsReturns);
    }

    @Test
    void shouldDenyReturnsWhenConfigurationIsMissing() {
        final OperatorConfigurationPort operatorConfigurationPort = mock(OperatorConfigurationPort.class);
        when(operatorConfigurationPort.getCurrent()).thenReturn(Optional.empty());
        final OperatorConfigurationServiceAdapter adapter = new OperatorConfigurationServiceAdapter(operatorConfigurationPort);

        final boolean supportsReturns = adapter.supportsReturnShipments();

        assertFalse(supportsReturns);
    }
}
