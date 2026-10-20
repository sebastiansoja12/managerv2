package com.warehouse.asyncjob.configuration;

import com.warehouse.asyncjob.api.ReadModelRebuildApiService;
import com.warehouse.asyncjob.application.port.primary.AsyncJobPort;
import com.warehouse.asyncjob.application.port.primary.AsyncJobPortImpl;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobSchedulerServicePort;
import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.application.service.AsyncJobExecutionService;
import com.warehouse.asyncjob.application.service.ReadModelRebuildServiceRegistry;
import com.warehouse.asyncjob.infrastructure.adapter.primary.ReadModelRebuildApiServiceAdapter;
import com.warehouse.asyncjob.infrastructure.adapter.primary.mapper.AsyncJobResponseMapper;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.AsyncJobJpaRepository;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.AsyncJobRepositoryImpl;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.AsyncJobSchedulerServiceAdapter;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.DeliveryReadModelRebuildServiceAdapter;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.DepartmentReadModelRebuildServiceAdapter;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.ShipmentReadModelRebuildServiceAdapter;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.mapper.AsyncJobEntityMapper;
import com.warehouse.logisticsdelivery.api.DeliveryReadModelRebuildApiService;
import com.warehouse.organisationstructure.department.api.DepartmentReadModelRebuildApiService;
import com.warehouse.shipment.infrastructure.ShipmentReadModelRebuildApiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Configuration
public class AsyncJobConfiguration {

    @Bean
    public AsyncJobRepository asyncJobRepository(final AsyncJobJpaRepository asyncJobJpaRepository) {
        return new AsyncJobRepositoryImpl(asyncJobJpaRepository, new AsyncJobEntityMapper());
    }

    @Bean("asyncJobTaskExecutor")
    public ThreadPoolTaskExecutor asyncJobTaskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("async-job-");
        return taskExecutor;
    }

    @Bean
    public AsyncJobSchedulerServicePort asyncJobSchedulerServicePort(
            @Qualifier("asyncJobTaskExecutor") final TaskExecutor taskExecutor) {
        return new AsyncJobSchedulerServiceAdapter(taskExecutor);
    }

    @Bean
    public ReadModelRebuildServicePort shipmentReadModelRebuildServicePort(
            final ShipmentReadModelRebuildApiService rebuildApiService) {
        return new ShipmentReadModelRebuildServiceAdapter(rebuildApiService);
    }

    @Bean
    public ReadModelRebuildServicePort departmentReadModelRebuildServicePort(
            final DepartmentReadModelRebuildApiService rebuildApiService) {
        return new DepartmentReadModelRebuildServiceAdapter(rebuildApiService);
    }

    @Bean
    public ReadModelRebuildServicePort deliveryReadModelRebuildServicePort(
            final DeliveryReadModelRebuildApiService rebuildApiService) {
        return new DeliveryReadModelRebuildServiceAdapter(rebuildApiService);
    }

    @Bean
    public ReadModelRebuildServiceRegistry readModelRebuildServiceRegistry(
            final List<ReadModelRebuildServicePort> rebuildServices) {
        return new ReadModelRebuildServiceRegistry(rebuildServices);
    }

    @Bean
    public AsyncJobExecutionService asyncJobExecutionService(final AsyncJobRepository asyncJobRepository,
                                                             final ReadModelRebuildServiceRegistry registry,
                                                             final TransactionTemplate transactionTemplate) {
        return new AsyncJobExecutionService(asyncJobRepository, registry, transactionTemplate);
    }

    @Bean
    public AsyncJobPort asyncJobPort(final AsyncJobRepository asyncJobRepository,
                                     final AsyncJobSchedulerServicePort asyncJobSchedulerServicePort,
                                     final AsyncJobExecutionService asyncJobExecutionService) {
        return new AsyncJobPortImpl(asyncJobRepository, asyncJobSchedulerServicePort, asyncJobExecutionService);
    }

    @Bean
    public AsyncJobResponseMapper asyncJobResponseMapper() {
        return new AsyncJobResponseMapper();
    }

    @Bean
    public ReadModelRebuildApiService readModelRebuildApiService(final AsyncJobPort asyncJobPort,
                                                                 final AsyncJobResponseMapper asyncJobResponseMapper) {
        return new ReadModelRebuildApiServiceAdapter(asyncJobPort, asyncJobResponseMapper);
    }
}
