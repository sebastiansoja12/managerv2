package com.warehouse.platformadministration.configuration;

import com.warehouse.auth.UserApiService;
import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.organisationstructure.api.OperatorApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.platformadministration.application.port.primary.OperatorDirectoryPort;
import com.warehouse.platformadministration.application.port.primary.OperatorDirectoryPortImpl;
import com.warehouse.platformadministration.application.port.secondary.OperatorCourierServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorDepartmentServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorUserServicePort;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorCourierServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorDepartmentServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorScopeExecutor;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorServiceAdapter;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.OperatorUserServiceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlatformAdministrationConfiguration {

    @Bean
    public OperatorDirectoryPort operatorDirectoryPort(final OperatorServicePort operatorServicePort,
                                                       final OperatorUserServicePort operatorUserServicePort,
                                                       final OperatorDepartmentServicePort operatorDepartmentServicePort,
                                                       final OperatorCourierServicePort operatorCourierServicePort) {
        return new OperatorDirectoryPortImpl(operatorServicePort, operatorUserServicePort,
                operatorDepartmentServicePort, operatorCourierServicePort);
    }

    @Bean
    public OperatorScopeExecutor operatorScopeExecutor(final OperatorContext operatorContext) {
        return new OperatorScopeExecutor(operatorContext);
    }

    @Bean("platformAdministration.operatorServicePort")
    public OperatorServicePort operatorServicePort(final OperatorApiService operatorApiService) {
        return new OperatorServiceAdapter(operatorApiService);
    }

    @Bean("platformAdministration.operatorUserServicePort")
    public OperatorUserServicePort operatorUserServicePort(final UserApiService userApiService,
                                                           final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorUserServiceAdapter(userApiService, operatorScopeExecutor);
    }

    @Bean("platformAdministration.operatorDepartmentServicePort")
    public OperatorDepartmentServicePort operatorDepartmentServicePort(final DepartmentApiService departmentApiService,
                                                                       final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorDepartmentServiceAdapter(departmentApiService, operatorScopeExecutor);
    }

    @Bean("platformAdministration.operatorCourierServicePort")
    public OperatorCourierServicePort operatorCourierServicePort(final SupplierApiService supplierApiService,
                                                                 final OperatorScopeExecutor operatorScopeExecutor) {
        return new OperatorCourierServiceAdapter(supplierApiService, operatorScopeExecutor);
    }
}
