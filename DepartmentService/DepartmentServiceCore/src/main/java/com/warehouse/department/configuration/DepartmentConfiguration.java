package com.warehouse.department.configuration;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.port.primary.DepartmentPortImpl;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.service.DepartmentServiceImpl;
import com.warehouse.department.infrastructure.adapter.primary.validator.DepartmentCreateApiDepartmentRequestValidator;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentRepositoryImpl;
import com.warehouse.department.infrastructure.adapter.secondary.TenantAdminProvisioningAdapter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentConfiguration {
    
	@Bean
	public DepartmentCreateApiDepartmentRequestValidator departmentRequestValidator() {
		return new DepartmentCreateApiDepartmentRequestValidator() {
		};
	}

    @Bean(name = "department.departmentRepository")
    public DepartmentRepository departmentRepository(final DepartmentReadRepository repository) {
        return new DepartmentRepositoryImpl(repository);
    }

    @Bean
    public DepartmentPort departmentPort(final DepartmentRepository departmentRepository, final DepartmentService departmentService,
                                         final TenantAdminProvisioningPort tenantAdminProvisioningPort) {
        return new DepartmentPortImpl(departmentRepository, departmentService, tenantAdminProvisioningPort);
    }

    @Bean
    public DepartmentService departmentService(final DepartmentRepository departmentRepository) {
        return new DepartmentServiceImpl(departmentRepository);
    }

    @Bean
    public TenantAdminProvisioningPort tenantAdminProvisioningPort(final ApplicationEventPublisher applicationEventPublisher) {
        return new TenantAdminProvisioningAdapter(applicationEventPublisher);
    }
}
