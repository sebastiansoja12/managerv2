package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationPortImplTest {

    @Mock
    private OperatorConfigurationService operatorConfigurationService;

    @Mock
    private CurrentOperatorService currentOperatorService;

    @Test
    void shouldGetConfigurationForCurrentOperator() {
        final OperatorConfiguration expectedConfiguration = OperatorTestFixtures.configuration();
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(expectedConfiguration));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final Optional<OperatorConfiguration> configuration = operatorConfigurationPort.getCurrent();

        assertEquals(Optional.of(expectedConfiguration), configuration);
        verify(currentOperatorService).getCurrentOperatorId();
        verify(operatorConfigurationService).getByOperatorId(OperatorTestFixtures.OPERATOR_ID);
    }

    @Test
    void shouldReturnEmptyWhenCurrentOperatorHasNoConfiguration() {
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.empty());
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final Optional<OperatorConfiguration> configuration = operatorConfigurationPort.getCurrent();

        assertTrue(configuration.isEmpty());
        verify(operatorConfigurationService).getByOperatorId(OperatorTestFixtures.OPERATOR_ID);
    }

    @Test
    void shouldCreateOperatorConfiguration() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration();
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        operatorConfigurationPort.create(OperatorTestFixtures.OPERATOR_ID, configuration);

        verify(operatorConfigurationService).create(OperatorTestFixtures.OPERATOR_ID, configuration);
    }

    @Test
    void shouldUpdateShipmentConfigurationForCurrentOperator() {
        final OperatorConfiguration currentConfiguration = OperatorTestFixtures.configuration();
        final OperatorConfiguration updatedConfiguration = new OperatorConfiguration(
                currentConfiguration.getShippingCapabilities(),
                OperatorTestFixtures.updatedShipmentConfiguration(),
                currentConfiguration.getDeliveryTimeConfiguration()
        );
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(currentConfiguration));
        when(operatorConfigurationService.create(
                org.mockito.ArgumentMatchers.eq(OperatorTestFixtures.OPERATOR_ID),
                any(OperatorConfiguration.class)
        )).thenAnswer(invocation -> invocation.getArgument(1));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final OperatorConfiguration result =
                operatorConfigurationPort.updateCurrentShipmentConfiguration(
                        OperatorTestFixtures.updatedShipmentConfiguration());

        final ArgumentCaptor<OperatorConfiguration> configurationCaptor =
                ArgumentCaptor.forClass(OperatorConfiguration.class);
        verify(operatorConfigurationService).create(
                org.mockito.ArgumentMatchers.eq(OperatorTestFixtures.OPERATOR_ID),
                configurationCaptor.capture()
        );
        assertEquals(result, configurationCaptor.getValue());
        assertEquals(
                updatedConfiguration.getShippingCapabilities(),
                configurationCaptor.getValue().getShippingCapabilities()
        );
        assertEquals(
                updatedConfiguration.getShipmentLimits().getMaxWeight(),
                configurationCaptor.getValue().getShipmentLimits().getMaxWeight()
        );
        assertEquals(
                updatedConfiguration.getDeliveryTimeConfiguration(),
                configurationCaptor.getValue().getDeliveryTimeConfiguration()
        );
    }
}
