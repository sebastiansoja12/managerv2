package com.warehouse.process.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.process.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.process.infrastructure.adapter.secondary.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.port.primary.ProcessPortImpl;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.domain.port.secondary.ProcessLogReadModelRepository;
import com.warehouse.process.domain.service.ProcessLogReadModelSyncService;
import com.warehouse.process.domain.service.ProcessLogReadModelSyncServiceImpl;
import com.warehouse.process.domain.service.ProcessService;
import com.warehouse.process.domain.service.ProcessServiceImpl;
import com.warehouse.process.infrastructure.adapter.primary.ProcessResourceAdapter;

@Configuration
public class ProcessConfiguration {

    @Bean
    public ProcessRepository processRepository(final ProcessLogReadRepository readRepository,
                                               final ProcessLogWriteRepository writeRepository) {
        return new ProcessRepositoryImpl(readRepository, writeRepository);
    }

    @Bean
    public ProcessLogReadModelRepository processLogReadModelRepository(
            final ProcessLogReadRepository processLogReadRepository,
            final CommunicationLogReadRepository communicationLogReadRepository) {
        return new ProcessLogReadModelRepositoryImpl(processLogReadRepository, communicationLogReadRepository);
    }

    @Bean
    public ProcessLogReadModelSyncService processLogReadModelSyncService(
            final ProcessLogReadModelRepository processLogReadModelRepository) {
        return new ProcessLogReadModelSyncServiceImpl(processLogReadModelRepository);
    }

    @Bean
    public ProcessPort processPort(final ProcessService processService) {
        return new ProcessPortImpl(processService);
    }

    @Bean
    public ProcessService processService(final ProcessRepository processRepository) {
        return new ProcessServiceImpl(processRepository);
    }

    @Bean
    public ProcessHubApiService processHubApiService(final ProcessPort processPort) {
        return new ProcessResourceAdapter(processPort);
    }

    @Bean
    public ProcessHubEventPublisher processHubEventPublisherPort(
            final ApplicationEventPublisher eventPublisher) {
        return new ProcessHubEventPublisherImpl(eventPublisher);
    }

    @Bean
    public CurrentUserServicePort currentUserServicePort(final CurrentUserApiService currentUserApiService) {
        return new CurrentUserServiceAdapter(currentUserApiService);
    }
}
