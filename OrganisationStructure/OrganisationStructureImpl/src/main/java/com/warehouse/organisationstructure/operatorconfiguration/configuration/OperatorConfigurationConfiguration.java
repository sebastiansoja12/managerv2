package com.warehouse.organisationstructure.operatorconfiguration.configuration;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPortImpl;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.OperatorConfigurationServiceAdapter;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationReadRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperatorConfigurationConfiguration {

    @Bean
    public OperatorConfigurationRepository operatorConfigurationRepository(
            final OperatorConfigurationReadRepository operatorConfigurationReadRepository) {
        return new OperatorConfigurationRepositoryImpl(operatorConfigurationReadRepository);
    }

    @Bean
    public OperatorConfigurationService operatorConfigurationService(
            final OperatorConfigurationRepository operatorConfigurationRepository) {
        return new OperatorConfigurationServiceImpl(operatorConfigurationRepository);
    }

    @Bean
    public OperatorConfigurationPort operatorConfigurationPort(
            final OperatorConfigurationService operatorConfigurationService,
            final CurrentOperatorService currentOperatorService) {
        return new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);
    }

    @Bean
    public OperatorConfigurationApiService operatorConfigurationApiService(
            final OperatorConfigurationPort operatorConfigurationPort) {
        return new OperatorConfigurationServiceAdapter(operatorConfigurationPort);
    }
}
