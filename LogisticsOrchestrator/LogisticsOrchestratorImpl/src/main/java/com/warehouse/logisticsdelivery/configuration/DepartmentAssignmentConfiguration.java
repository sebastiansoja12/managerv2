package com.warehouse.logisticsdelivery.configuration;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.repository.BaseRepository;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.logisticsdelivery.application.listener.DepartmentAssignmentRunListener;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPort;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPortImpl;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunExecutionServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunMetrics;
import com.warehouse.logisticsdelivery.application.port.secondary.CourierServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentConfigServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.ShipmentServicePort;
import com.warehouse.logisticsdelivery.application.service.AssignmentBatchAssigner;
import com.warehouse.logisticsdelivery.application.service.AssignmentOrchestrator;
import com.warehouse.logisticsdelivery.application.service.AssignmentRunProcessor;
import com.warehouse.logisticsdelivery.application.service.CourierRosterLoader;
import com.warehouse.logisticsdelivery.domain.service.AssignmentWindowCalculator;
import com.warehouse.logisticsdelivery.domain.service.DepartmentAssignmentRunFactory;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.AssignmentCleanupJob;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.AssignmentOrchestratorJob;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.AssignmentRecoveryJob;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.DepartmentAssignmentApiServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DepartmentAssignmentRunApiMapper;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.AssignmentRunExecutionServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.CourierServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.DepartmentAssignmentConfigServiceAdapter;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.DepartmentAssignmentRunRepositoryImpl;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.MicrometerAssignmentRunMetrics;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.DepartmentAssignmentRunEntity;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.mapper.DepartmentAssignmentRunEntityMapper;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class DepartmentAssignmentConfiguration {

    private static final String JOB_GROUP = "courier-assignment";

    @Bean
    public CourierServicePort courierServicePort(final SupplierApiService supplierApiService,
                                                 final SupplierConfigurationApiService supplierConfigurationApiService) {
        return new CourierServiceAdapter(supplierApiService, supplierConfigurationApiService);
    }

    @Bean
    public DepartmentAssignmentConfigServicePort departmentAssignmentConfigServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService,
            final DepartmentApiService departmentApiService) {
        return new DepartmentAssignmentConfigServiceAdapter(operatorConfigurationApiService, departmentApiService,
                Clock.systemDefaultZone().getZone());
    }

    @Bean
    public AssignmentRunMetrics assignmentRunMetrics(final MeterRegistry meterRegistry) {
        return new MicrometerAssignmentRunMetrics(meterRegistry);
    }

    @Bean
    public BaseRepository<DepartmentAssignmentRunEntity> departmentAssignmentRunEntityRepository(
            final EntityManager entityManager,
            final OperatorContextProvider operatorContextProvider) {
        return new BaseRepository<>(entityManager, operatorContextProvider);
    }

    @Bean
    public DepartmentAssignmentRunRepository departmentAssignmentRunRepository(
            final BaseRepository<DepartmentAssignmentRunEntity> departmentAssignmentRunEntityRepository) {
        return new DepartmentAssignmentRunRepositoryImpl(departmentAssignmentRunEntityRepository,
                new DepartmentAssignmentRunEntityMapper());
    }

    @Bean
    public CourierRosterLoader courierRosterLoader(final CourierServicePort courierServicePort,
                                                   final DeliveryRepository deliveryRepository,
                                                   final PlatformTransactionManager transactionManager) {
        final TransactionTemplate readOnly = new TransactionTemplate(transactionManager);
        readOnly.setReadOnly(true);
        return new CourierRosterLoader(courierServicePort, deliveryRepository, readOnly);
    }

    @Bean("courierAssignmentTaskExecutor")
    public ThreadPoolTaskExecutor courierAssignmentTaskExecutor(
            @Value("${manager.delivery.courier-assignment.workers:2}") final int workers,
            @Value("${manager.delivery.courier-assignment.queue-capacity:500}") final int queueCapacity) {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(workers);
        taskExecutor.setMaxPoolSize(workers);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setThreadNamePrefix("courier-assignment-");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(30);
        return taskExecutor;
    }

    @Bean
    public AssignmentRunExecutionServicePort assignmentRunExecutionServicePort(
            @Qualifier("courierAssignmentTaskExecutor") final TaskExecutor taskExecutor,
            final OperatorContext operatorContext,
            final OperatorContextProvider operatorContextProvider,
            final ObjectProvider<DepartmentAssignmentPort> departmentAssignmentPort) {
        return new AssignmentRunExecutionServiceAdapter(taskExecutor, operatorContext, operatorContextProvider,
                departmentAssignmentPort);
    }

    @Bean
    public DepartmentAssignmentRunListener departmentAssignmentRunListener(
            final AssignmentRunExecutionServicePort assignmentRunExecutionServicePort) {
        return new DepartmentAssignmentRunListener(assignmentRunExecutionServicePort);
    }

    @Bean
    public DepartmentAssignmentPort departmentAssignmentPort(
            final DepartmentAssignmentConfigServicePort departmentAssignmentConfigServicePort,
            final DepartmentAssignmentRunRepository departmentAssignmentRunRepository,
            final DeliveryRepository deliveryRepository,
            @Qualifier("logisticsDelivery.shipmentServicePort") final ShipmentServicePort shipmentServicePort,
            final CourierRosterLoader courierRosterLoader,
            final DomainEventPublisher domainEventPublisher,
            final AssignmentRunExecutionServicePort assignmentRunExecutionServicePort,
            final AssignmentRunMetrics assignmentRunMetrics,
            final PlatformTransactionManager transactionManager,
            @Value("${manager.delivery.courier-assignment.transaction-timeout-seconds:60}") final int transactionTimeout,
            @Value("${manager.delivery.courier-assignment.run.lease:5m}") final Duration lease,
            @Value("${manager.delivery.courier-assignment.run.max-retries:3}") final int maxRetries,
            @Value("${manager.delivery.courier-assignment.run.info-log-every-batches:10}") final int infoLogEvery,
            @Value("${manager.delivery.courier-assignment.run.pending-recovery-delay:5m}") final Duration pendingDelay,
            @Value("${manager.delivery.courier-assignment.run.retention:30d}") final Duration retention,
            @Value("${manager.delivery.courier-assignment.run.recovery-limit:50}") final int recoveryLimit) {
        final Clock clock = Clock.systemDefaultZone();
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setTimeout(transactionTimeout);
        final AssignmentOrchestrator orchestrator = new AssignmentOrchestrator(departmentAssignmentConfigServicePort,
                departmentAssignmentRunRepository, new DepartmentAssignmentRunFactory(clock),
                new AssignmentWindowCalculator(), domainEventPublisher, transactionTemplate, clock);
        final AssignmentRunProcessor processor = new AssignmentRunProcessor(departmentAssignmentRunRepository,
                deliveryRepository, courierRosterLoader, new AssignmentBatchAssigner(deliveryRepository,
                shipmentServicePort, domainEventPublisher, clock), assignmentRunMetrics, transactionTemplate, clock,
                workerId(), lease, maxRetries, infoLogEvery);
        return new DepartmentAssignmentPortImpl(orchestrator, processor, departmentAssignmentRunRepository,
                assignmentRunExecutionServicePort, assignmentRunMetrics, transactionTemplate, clock, pendingDelay,
                retention, recoveryLimit);
    }

    @Bean
    public DepartmentAssignmentApiService departmentAssignmentApiService(
            final DepartmentAssignmentPort departmentAssignmentPort,
            final OperatorContext operatorContext) {
        return new DepartmentAssignmentApiServiceAdapter(departmentAssignmentPort, operatorContext,
                new DepartmentAssignmentRunApiMapper());
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public JobDetailFactoryBean assignmentOrchestratorJobDetail() {
        return jobDetail(AssignmentOrchestratorJob.class, "assignmentOrchestratorJob");
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public CronTriggerFactoryBean assignmentOrchestratorTrigger(
            @Qualifier("assignmentOrchestratorJobDetail") final JobDetail jobDetail,
            @Value("${manager.delivery.courier-assignment.cron:0 */30 * * * ?}") final String cron) {
        return trigger(jobDetail, "assignmentOrchestratorTrigger", cron);
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public JobDetailFactoryBean assignmentRecoveryJobDetail() {
        return jobDetail(AssignmentRecoveryJob.class, "assignmentRecoveryJob");
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public CronTriggerFactoryBean assignmentRecoveryTrigger(
            @Qualifier("assignmentRecoveryJobDetail") final JobDetail jobDetail,
            @Value("${manager.delivery.courier-assignment.recovery-cron:0 */5 * * * ?}") final String cron) {
        return trigger(jobDetail, "assignmentRecoveryTrigger", cron);
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public JobDetailFactoryBean assignmentCleanupJobDetail() {
        return jobDetail(AssignmentCleanupJob.class, "assignmentCleanupJob");
    }

    @Bean
    @ConditionalOnProperty(name = "manager.delivery.courier-assignment.enabled", havingValue = "true",
            matchIfMissing = true)
    public CronTriggerFactoryBean assignmentCleanupTrigger(
            @Qualifier("assignmentCleanupJobDetail") final JobDetail jobDetail,
            @Value("${manager.delivery.courier-assignment.cleanup-cron:0 0 0 * * ?}") final String cron) {
        return trigger(jobDetail, "assignmentCleanupTrigger", cron);
    }

    private static JobDetailFactoryBean jobDetail(final Class<? extends org.quartz.Job> jobClass, final String name) {
        final JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(jobClass);
        factory.setName(name);
        factory.setGroup(JOB_GROUP);
        factory.setDurability(true);
        return factory;
    }

    private static CronTriggerFactoryBean trigger(final JobDetail jobDetail, final String name, final String cron) {
        final CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
        factory.setJobDetail(jobDetail);
        factory.setName(name);
        factory.setGroup(JOB_GROUP);
        factory.setCronExpression(cron);
        factory.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
        return factory;
    }

    private static String workerId() {
        try {
            return InetAddress.getLocalHost().getHostName() + ":" + UUID.randomUUID();
        } catch (final UnknownHostException exception) {
            return "worker:" + UUID.randomUUID();
        }
    }
}
