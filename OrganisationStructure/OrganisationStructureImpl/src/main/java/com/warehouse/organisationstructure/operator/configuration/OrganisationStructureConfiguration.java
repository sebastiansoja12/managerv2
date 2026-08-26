package com.warehouse.organisationstructure.operator.configuration;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.OperatorUserEventPublisher;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPort;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPortImpl;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorContextServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorGeocodingConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.service.OperatorServiceImpl;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.primary.OperatorServiceAdapter;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorConfigurationKafkaServiceAdapter;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorContextServiceAdapter;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorDepartmentNotifyAdapter;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorGeocodingConfigurationEventServiceAdapter;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorReadRepository;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorRepositoryImpl;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorUserNotifyAdapter;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;

@Configuration
public class OrganisationStructureConfiguration {

    @Bean
    public OperatorRepository operatorRepository(final OperatorReadRepository operatorReadRepository) {
        return new OperatorRepositoryImpl(operatorReadRepository);
    }

    @Bean
    public OperatorService operatorService(final OperatorRepository operatorRepository,
                                           final OperatorConfigurationService operatorConfigurationService) {
        return new OperatorServiceImpl(operatorRepository, operatorConfigurationService);
    }

    @Bean
    public OperatorDepartmentNotifyPort operatorDepartmentNotifyPort(
            final ApplicationEventPublisher applicationEventPublisher) {
        return new OperatorDepartmentNotifyAdapter(applicationEventPublisher);
    }

    @Bean
    public OperatorGeocodingConfigurationEventServicePort operatorGeocodingConfigurationEventServicePort(
            final ApplicationEventPublisher applicationEventPublisher) {
        return new OperatorGeocodingConfigurationEventServiceAdapter(applicationEventPublisher);
    }

    @Bean
    public OperatorConfigurationEventServicePort operatorConfigurationEventServicePort(
            final KafkaTemplateClient kafkaTemplateClient) {
        return new OperatorConfigurationKafkaServiceAdapter(kafkaTemplateClient);
    }

    @Bean
    public OperatorContextServicePort operatorContextServicePort(final OperatorContext operatorContext) {
        return new OperatorContextServiceAdapter(operatorContext);
    }

    @Bean
    public OperatorUserNotifyPort operatorUserNotifyPort(final OperatorUserEventPublisher operatorUserEventPublisher) {
        return new OperatorUserNotifyAdapter(operatorUserEventPublisher);
    }

    @Bean
    public OperatorPort operatorPort(final OperatorService operatorService) {
        return new OperatorPortImpl(operatorService);
    }

    @Bean
    public OperatorApiService operatorApiService(final OperatorPort operatorPort) {
        return new OperatorServiceAdapter(operatorPort);
    }
}
