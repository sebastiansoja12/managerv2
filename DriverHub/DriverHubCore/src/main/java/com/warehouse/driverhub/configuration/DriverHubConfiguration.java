package com.warehouse.driverhub.configuration;

import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.driverhub.api.CarrierApiService;
import com.warehouse.driverhub.application.port.primary.CarrierPort;
import com.warehouse.driverhub.application.port.primary.CarrierPortImpl;
import com.warehouse.driverhub.application.port.primary.DriverPort;
import com.warehouse.driverhub.application.port.primary.DriverPortImpl;
import com.warehouse.driverhub.application.port.secondary.CarrierRepository;
import com.warehouse.driverhub.application.port.secondary.CurrentOperatorServicePort;
import com.warehouse.driverhub.infrastructure.adapter.primary.CarrierApiServiceAdapter;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.driverhub.infrastructure.adapter.secondary.CarrierDriverReadRepository;
import com.warehouse.driverhub.infrastructure.adapter.secondary.CarrierRepositoryImpl;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import com.warehouse.driverhub.infrastructure.adapter.secondary.CurrentOperatorServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DriverHubConfiguration {

    @Bean
    public BaseRepository<CarrierEntity> driverHubCarrierEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public CarrierRepository carrierRepository(
            @Qualifier("driverHubCarrierEntityRepository") final BaseRepository<CarrierEntity> repository,
            final CarrierDriverReadRepository carrierDriverReadRepository) {
        return new CarrierRepositoryImpl(repository, carrierDriverReadRepository);
    }

    @Bean
    public CurrentOperatorServicePort driverHubCurrentOperatorServicePort(final OperatorContextProvider operatorContextProvider) {
        return new CurrentOperatorServiceAdapter(operatorContextProvider);
    }

    @Bean
    public CarrierPort carrierPort(final CarrierRepository carrierRepository,
                                   final CurrentOperatorServicePort driverHubCurrentOperatorServicePort) {
        return new CarrierPortImpl(carrierRepository, driverHubCurrentOperatorServicePort);
    }

    @Bean
    public DriverPort driverPort(final CarrierRepository carrierRepository,
                                 final CurrentOperatorServicePort driverHubCurrentOperatorServicePort) {
        return new DriverPortImpl(carrierRepository, driverHubCurrentOperatorServicePort);
    }

    @Bean
    public CarrierApiService carrierApiService(final CarrierPort carrierPort) {
        return new CarrierApiServiceAdapter(carrierPort);
    }
}
