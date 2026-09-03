package com.warehouse.platformadministration.configuration;

import com.warehouse.infrastructure.GeocodingApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.terminal.DeviceApiService;
import com.warehouse.platformadministration.application.port.primary.OperatorDataPort;
import com.warehouse.platformadministration.application.port.primary.OperatorDataPortImpl;
import com.warehouse.platformadministration.application.port.primary.OperatorDevicePort;
import com.warehouse.platformadministration.application.port.primary.OperatorDevicePortImpl;
import com.warehouse.platformadministration.application.port.primary.OperatorOnboardingPort;
import com.warehouse.platformadministration.application.port.primary.OperatorOnboardingPortImpl;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDataErasureRepository;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDeviceServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorGeocodingServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorDataErasureReadRepository;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorDataErasureRepositoryImpl;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorDeviceServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorGeocodingServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorScopeExecutor;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorShipmentServiceAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PlatformOperationsConfiguration {

    @Bean("platformAdministration.operatorDeviceServicePort")
    public OperatorDeviceServicePort operatorDeviceServicePort(final DeviceApiService deviceApiService,
                                                               final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorDeviceServiceAdapter(deviceApiService, operatorScopeExecutor);
    }

    @Bean("platformAdministration.operatorGeocodingServicePort")
    public OperatorGeocodingServicePort operatorGeocodingServicePort(final GeocodingApiService geocodingApiService,
                                                                     final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorGeocodingServiceAdapter(geocodingApiService, operatorScopeExecutor);
    }

    @Bean("platformAdministration.operatorShipmentServicePort")
    public OperatorShipmentServicePort operatorShipmentServicePort(final ShipmentApiService shipmentApiService,
                                                                   final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorShipmentServiceAdapter(shipmentApiService, operatorScopeExecutor);
    }

    @Bean
    public OperatorDataErasureRepository operatorDataErasureRepository(
            final OperatorDataErasureReadRepository operatorDataErasureReadRepository) {
        return new OperatorDataErasureRepositoryImpl(operatorDataErasureReadRepository);
    }

    @Bean
    public OperatorOnboardingPort operatorOnboardingPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final OperatorUserServicePort operatorUserServicePort,
            final OperatorDepartmentServicePort operatorDepartmentServicePort,
            final OperatorCourierServicePort operatorCourierServicePort,
            final OperatorGeocodingServicePort operatorGeocodingServicePort,
            final OperatorShipmentServicePort operatorShipmentServicePort) {
        return new OperatorOnboardingPortImpl(operatorServicePort, operatorUserServicePort,
                operatorDepartmentServicePort, operatorCourierServicePort, operatorGeocodingServicePort,
                operatorShipmentServicePort);
    }

    @Bean
    public OperatorDevicePort operatorDevicePort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final OperatorDeviceServicePort operatorDeviceServicePort,
            final OperatorUserServicePort operatorUserServicePort) {
        return new OperatorDevicePortImpl(operatorServicePort, operatorDeviceServicePort, operatorUserServicePort);
    }

    @Bean
    public OperatorDataPort operatorDataPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final OperatorUserServicePort operatorUserServicePort,
            final OperatorDepartmentServicePort operatorDepartmentServicePort,
            final OperatorCourierServicePort operatorCourierServicePort,
            final OperatorDeviceServicePort operatorDeviceServicePort,
            final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort,
            final OperatorSessionServicePort operatorSessionServicePort,
            final PlatformAuditRepository platformAuditRepository,
            final OperatorDataErasureRepository operatorDataErasureRepository,
            final PlatformActorServicePort platformActorServicePort) {
        return new OperatorDataPortImpl(operatorServicePort, operatorUserServicePort, operatorDepartmentServicePort,
                operatorCourierServicePort, operatorDeviceServicePort, operatorShipmentSettingsServicePort,
                operatorSessionServicePort, platformAuditRepository, operatorDataErasureRepository,
                platformActorServicePort, Clock.systemUTC());
    }
}
