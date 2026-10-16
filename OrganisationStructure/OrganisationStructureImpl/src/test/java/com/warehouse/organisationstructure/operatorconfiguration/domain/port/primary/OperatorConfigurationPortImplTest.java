package com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.CurrencyCode;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DuplicateScanPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.FleetConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.LengthUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DefaultPickupPointType;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RegionalDateFormat;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ScanErrorFeedback;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.SortingConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.UnresolvedDestinationPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.WeightUnit;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentLimits;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.NotificationChannel;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentNotificationConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
    void shouldReturnEmptyWhenCurrentUserHasNoOperator() {
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final Optional<OperatorConfiguration> configuration = operatorConfigurationPort.getCurrent();

        assertTrue(configuration.isEmpty());
    }

    @Test
    void shouldGetNotificationConfigurationForGivenOperatorWithoutSecurityContext() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration();
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(configuration));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final ShipmentNotificationConfiguration notificationConfiguration =
                operatorConfigurationPort.getShipmentNotificationConfiguration(OperatorTestFixtures.OPERATOR_ID);

        assertEquals(configuration.getShipmentConfiguration().getNotificationConfiguration(), notificationConfiguration);
        verifyNoInteractions(currentOperatorService);
    }

    @Test
    void shouldFallBackToDefaultNotificationConfigurationWhenOperatorHasNoConfiguration() {
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.empty());
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final ShipmentNotificationConfiguration notificationConfiguration =
                operatorConfigurationPort.getShipmentNotificationConfiguration(OperatorTestFixtures.OPERATOR_ID);

        assertEquals(NotificationChannel.SMS, notificationConfiguration.getNotificationChannel());
        assertTrue(notificationConfiguration.isNotifyRecipientOnCreated());
    }

    @Test
    void shouldUpdateOnlyShipmentLimitsOfCurrentOperator() {
        final OperatorConfiguration configuration = OperatorTestFixtures.configuration();
        final ShipmentConfiguration currentShipmentConfiguration = configuration.getShipmentConfiguration();
        final ShipmentLimits shipmentLimits = new ShipmentLimits(10.0, 0.0, 50.0, 40.0, 30.0, 0.0, true);
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(configuration));
        when(operatorConfigurationService.create(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(1));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final ShipmentConfiguration shipmentConfiguration =
                operatorConfigurationPort.updateCurrentShipmentLimits(shipmentLimits);

        assertSame(shipmentLimits, shipmentConfiguration.getShipmentLimits());
        assertSame(currentShipmentConfiguration.getValidationConfiguration(),
                shipmentConfiguration.getValidationConfiguration());
        assertSame(currentShipmentConfiguration.getLabelConfiguration(), shipmentConfiguration.getLabelConfiguration());
        assertSame(currentShipmentConfiguration.getTrackingNumberRule(), shipmentConfiguration.getTrackingNumberRule());
        assertSame(currentShipmentConfiguration.getNotificationConfiguration(),
                shipmentConfiguration.getNotificationConfiguration());
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

    @Test
    void shouldUpdateRegionalConfigurationAndKeepOtherSections() {
        final OperatorConfiguration currentConfiguration = OperatorTestFixtures.configuration();
        final RegionalConfiguration regionalConfiguration = new RegionalConfiguration("Europe/Berlin",
                RegionalDateFormat.YYYY_MM_DD, CurrencyCode.EUR, WeightUnit.KG, LengthUnit.CM, "07:00", "15:00",
                true, false);
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(currentConfiguration));
        when(operatorConfigurationService.create(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(1));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final RegionalConfiguration updated =
                operatorConfigurationPort.updateCurrentRegionalConfiguration(regionalConfiguration);

        final ArgumentCaptor<OperatorConfiguration> saved = ArgumentCaptor.forClass(OperatorConfiguration.class);
        verify(operatorConfigurationService).create(any(), saved.capture());
        assertSame(regionalConfiguration, updated);
        assertSame(currentConfiguration.getShipmentConfiguration(), saved.getValue().getShipmentConfiguration());
    }

    @Test
    void shouldReturnDefaultFleetConfigurationWhenOperatorHasNoConfiguration() {
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.empty());
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final FleetConfiguration configuration = operatorConfigurationPort.getCurrentFleetConfiguration();

        assertEquals(FleetConfiguration.defaultConfiguration().getMaxDriverShiftHours(),
                configuration.getMaxDriverShiftHours());
    }

    @Test
    void shouldUpdateSortingConfigurationAndKeepOtherSections() {
        final OperatorConfiguration currentConfiguration = OperatorTestFixtures.configuration();
        final SortingConfiguration sortingConfiguration = new SortingConfiguration(true, true, false,
                DuplicateScanPolicy.BLOCK, UnresolvedDestinationPolicy.ROUTE_TO_EXCEPTION_RAMP, "R-01", true,
                ScanErrorFeedback.SOUND, 6);
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.of(currentConfiguration));
        when(operatorConfigurationService.create(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(1));
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final SortingConfiguration updated =
                operatorConfigurationPort.updateCurrentSortingConfiguration(sortingConfiguration);

        final ArgumentCaptor<OperatorConfiguration> saved = ArgumentCaptor.forClass(OperatorConfiguration.class);
        verify(operatorConfigurationService).create(any(), saved.capture());
        assertSame(sortingConfiguration, updated);
        assertSame(currentConfiguration.getShipmentConfiguration(), saved.getValue().getShipmentConfiguration());
    }

    @Test
    void shouldReturnDefaultPickupPointConfigurationWhenOperatorHasNoConfiguration() {
        when(currentOperatorService.getCurrentOperatorId()).thenReturn(OperatorTestFixtures.OPERATOR_ID);
        when(operatorConfigurationService.getByOperatorId(OperatorTestFixtures.OPERATOR_ID))
                .thenReturn(Optional.empty());
        final OperatorConfigurationPort operatorConfigurationPort =
                new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);

        final PickupPointConfiguration configuration = operatorConfigurationPort.getCurrentPickupPointConfiguration();

        assertEquals(6, configuration.getCollectionPolicy().getPickupCodeLength());
        assertEquals(DefaultPickupPointType.SERVICE_POINT, configuration.getDefaultsPolicy().getDefaultType());
    }
}
