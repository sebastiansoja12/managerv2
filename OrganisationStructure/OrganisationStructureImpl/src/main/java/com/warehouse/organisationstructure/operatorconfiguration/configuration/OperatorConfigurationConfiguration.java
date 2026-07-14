package com.warehouse.organisationstructure.operatorconfiguration.configuration;

import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;
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
}
