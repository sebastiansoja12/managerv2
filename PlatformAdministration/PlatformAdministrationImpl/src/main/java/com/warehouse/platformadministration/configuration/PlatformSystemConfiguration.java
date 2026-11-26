package com.warehouse.platformadministration.configuration;

import tools.jackson.databind.ObjectMapper;
import com.warehouse.asyncjob.api.ReadModelRebuildApiService;
import com.warehouse.auth.OperatorSessionApiService;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.platformadministration.application.port.primary.OperatorAdminActionPort;
import com.warehouse.platformadministration.application.port.primary.OperatorAdminActionPortImpl;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.platformadministration.application.port.secondary.DepartmentAssignmentRunServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorJobServicePort;
import com.warehouse.platformadministration.application.service.OperatorJobRegistry;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.CourierAssignmentOrchestrationJobServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.CourierAssignmentRecoveryJobServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.CourierAssignmentRunCleanupJobServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.DepartmentAssignmentRunServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.DangerousGoodValidationJobServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.DriverLicenseValidationJobServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.InactiveDepartmentArchiveJobServiceAdapter;
import com.warehouse.supplier.api.SupplierValidityApiService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.platformadministration.application.port.primary.MaintenanceModePort;
import com.warehouse.platformadministration.application.port.primary.MaintenanceModePortImpl;
import com.warehouse.platformadministration.application.port.primary.OperatorAccessPort;
import com.warehouse.platformadministration.application.port.primary.OperatorAccessPortImpl;
import com.warehouse.platformadministration.application.port.primary.OperatorSettingsPort;
import com.warehouse.platformadministration.application.port.primary.OperatorSettingsPortImpl;
import com.warehouse.platformadministration.application.port.primary.ReadModelRebuildPort;
import com.warehouse.platformadministration.application.port.primary.ReadModelRebuildPortImpl;
import com.warehouse.platformadministration.application.port.primary.SystemStatusPort;
import com.warehouse.platformadministration.application.port.primary.SystemStatusPortImpl;
import com.warehouse.platformadministration.application.port.secondary.DatabaseStatusServicePort;
import com.warehouse.platformadministration.application.port.secondary.MaintenanceModeRepository;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorSessionServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorShipmentSettingsServicePort;
import com.warehouse.platformadministration.application.port.secondary.PlatformActorServicePort;
import com.warehouse.platformadministration.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.platformadministration.application.port.secondary.RuntimeStatusServicePort;
import com.warehouse.platformadministration.infrastructure.adapter.primary.MaintenanceModeInterceptor;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.DatabaseStatusServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.MaintenanceModeRepositoryImpl;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorScopeExecutor;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorSessionServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorShipmentSettingsServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.PlatformMaintenanceReadRepository;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.ReadModelRebuildServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.RuntimeStatusServiceAdapter;
import org.springframework.beans.factory.ObjectProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.time.Clock;

@Configuration
public class PlatformSystemConfiguration {

    @Bean("platformAdministration.operatorSessionServicePort")
    public OperatorSessionServicePort operatorSessionServicePort(final OperatorSessionApiService operatorSessionApiService) {
        return new OperatorSessionServiceAdapter(operatorSessionApiService);
    }

    @Bean
    public OperatorAccessPort operatorAccessPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final OperatorSessionServicePort operatorSessionServicePort) {
        return new OperatorAccessPortImpl(operatorServicePort, operatorSessionServicePort);
    }

    @Bean
    public MaintenanceModeRepository maintenanceModeRepository(final PlatformMaintenanceReadRepository repository) {
        return new MaintenanceModeRepositoryImpl(repository, Clock.systemUTC());
    }

    @Bean
    public MaintenanceModePort maintenanceModePort(final MaintenanceModeRepository maintenanceModeRepository,
                                                   final PlatformActorServicePort platformActorServicePort) {
        return new MaintenanceModePortImpl(maintenanceModeRepository, platformActorServicePort, Clock.systemUTC());
    }

    @Bean
    public RuntimeStatusServicePort runtimeStatusServicePort(final Environment environment,
                                                             final ObjectProvider<BuildProperties> buildProperties) {
        return new RuntimeStatusServiceAdapter(environment, buildProperties, Clock.systemUTC());
    }

    @Bean
    public DatabaseStatusServicePort databaseStatusServicePort(final DataSource dataSource,
                                                               final JdbcTemplate jdbcTemplate) {
        return new DatabaseStatusServiceAdapter(dataSource, jdbcTemplate);
    }

    @Bean
    public SystemStatusPort systemStatusPort(final RuntimeStatusServicePort runtimeStatusServicePort,
                                             final DatabaseStatusServicePort databaseStatusServicePort,
                                             final MaintenanceModeRepository maintenanceModeRepository) {
        return new SystemStatusPortImpl(runtimeStatusServicePort, databaseStatusServicePort, maintenanceModeRepository);
    }

    @Bean
    public ReadModelRebuildServicePort readModelRebuildServicePort(
            final ReadModelRebuildApiService readModelRebuildApiService) {
        return new ReadModelRebuildServiceAdapter(readModelRebuildApiService);
    }

    @Bean
    public OperatorJobServicePort courierAssignmentOrchestrationJobServicePort(
            final DepartmentAssignmentApiService departmentAssignmentApiService) {
        return new CourierAssignmentOrchestrationJobServiceAdapter(departmentAssignmentApiService);
    }

    @Bean
    public OperatorJobServicePort courierAssignmentRecoveryJobServicePort(
            final DepartmentAssignmentApiService departmentAssignmentApiService) {
        return new CourierAssignmentRecoveryJobServiceAdapter(departmentAssignmentApiService);
    }

    @Bean
    public OperatorJobServicePort courierAssignmentRunCleanupJobServicePort(
            final DepartmentAssignmentApiService departmentAssignmentApiService) {
        return new CourierAssignmentRunCleanupJobServiceAdapter(departmentAssignmentApiService);
    }

    @Bean
    public OperatorJobServicePort inactiveDepartmentArchiveJobServicePort(
            final DepartmentApiService departmentApiService,
            final OperatorScopeExecutor operatorScopeExecutor) {
        return new InactiveDepartmentArchiveJobServiceAdapter(departmentApiService, operatorScopeExecutor);
    }

    @Bean
    public OperatorJobServicePort driverLicenseValidationJobServicePort(
            final SupplierValidityApiService supplierValidityApiService) {
        return new DriverLicenseValidationJobServiceAdapter(supplierValidityApiService);
    }

    @Bean
    public OperatorJobServicePort dangerousGoodValidationJobServicePort(
            final SupplierValidityApiService supplierValidityApiService) {
        return new DangerousGoodValidationJobServiceAdapter(supplierValidityApiService);
    }

    @Bean
    public DepartmentAssignmentRunServicePort departmentAssignmentRunServicePort(
            final DepartmentAssignmentApiService departmentAssignmentApiService) {
        return new DepartmentAssignmentRunServiceAdapter(departmentAssignmentApiService);
    }

    @Bean
    public OperatorAdminActionPort operatorAdminActionPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final List<OperatorJobServicePort> operatorJobServicePorts,
            final DepartmentAssignmentRunServicePort departmentAssignmentRunServicePort) {
        return new OperatorAdminActionPortImpl(operatorServicePort, new OperatorJobRegistry(operatorJobServicePorts),
                departmentAssignmentRunServicePort);
    }

    @Bean
    public ReadModelRebuildPort readModelRebuildPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final ReadModelRebuildServicePort readModelRebuildServicePort) {
        return new ReadModelRebuildPortImpl(operatorServicePort, readModelRebuildServicePort);
    }

    @Bean
    public OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService,
            final OperatorScopeExecutor operatorScopeExecutor,
            final ObjectMapper objectMapper) {
        return new OperatorShipmentSettingsServiceAdapter(operatorConfigurationApiService, operatorScopeExecutor,
                objectMapper);
    }

    @Bean
    public OperatorSettingsPort operatorSettingsPort(
            @Qualifier("platformAdministration.operatorServicePort") final OperatorServicePort operatorServicePort,
            final OperatorShipmentSettingsServicePort operatorShipmentSettingsServicePort) {
        return new OperatorSettingsPortImpl(operatorServicePort, operatorShipmentSettingsServicePort);
    }

    @Bean
    public WebMvcConfigurer maintenanceModeWebMvcConfigurer(final MaintenanceModePort maintenanceModePort,
                                                            final ObjectMapper objectMapper) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(final InterceptorRegistry registry) {
                registry.addInterceptor(new MaintenanceModeInterceptor(maintenanceModePort, objectMapper))
                        .order(Ordered.HIGHEST_PRECEDENCE);
            }
        };
    }
}
