package com.warehouse.department.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.port.primary.DepartmentPortImpl;
import com.warehouse.department.domain.port.secondary.DepartmentCoordinatesServicePort;
import com.warehouse.department.domain.port.secondary.DepartmentReadRepository;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.port.secondary.UserClientServicePort;
import com.warehouse.department.domain.service.AuthenticationService;
import com.warehouse.department.domain.service.AuthenticationServiceImpl;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.service.DepartmentServiceImpl;
import com.warehouse.department.domain.validator.Validator;
import com.warehouse.department.infrastructure.adapter.primary.DepartmentServiceAdapter;
import com.warehouse.department.infrastructure.adapter.primary.validator.DepartmentCreateApiDepartmentRequestValidator;
import com.warehouse.department.infrastructure.adapter.secondary.*;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.entity.readmodel.DepartmentReadEntity;
import com.warehouse.voronoi.VoronoiCoordinatesService;

@Configuration
public class DepartmentConfiguration {
    
	@Bean
	public DepartmentCreateApiDepartmentRequestValidator departmentRequestValidator() {
		return new DepartmentCreateApiDepartmentRequestValidator() {
		};
    }

    @Bean(name = "department.departmentRepository")
    public DepartmentRepository departmentRepository(
            final OperatorFilteredRepository<DepartmentEntity> repository,
            final DepartmentReadRepository<DepartmentReadEntity> readRepository) {
        return new DepartmentRepositoryImpl(repository, readRepository);
    }

    @Bean
    public DepartmentPort departmentPort(final DepartmentRepository departmentRepository,
                                         final DepartmentService departmentService,
                                         final TenantAdminProvisioningPort tenantAdminProvisioningPort,
                                         final Validator validator,
                                         final DepartmentCoordinatesServicePort departmentCoordinatesServicePort) {
        return new DepartmentPortImpl(departmentRepository, departmentService, tenantAdminProvisioningPort, validator,
                departmentCoordinatesServicePort);
    }

    @Bean(name = "department.authenticationService")
    public AuthenticationService authenticationService() {
        return new AuthenticationServiceImpl();
    }

    @Bean
    public Validator validator() {
        return new Validator() {
        };
    }

    @Bean
    public DepartmentApiService departmentApiService(final DepartmentPort departmentPort) {
        return new DepartmentServiceAdapter(departmentPort);
    }

    @Bean
    public DepartmentService departmentService(final DepartmentRepository departmentRepository) {
        return new DepartmentServiceImpl(departmentRepository);
    }

    @Bean
    public TenantAdminProvisioningPort tenantAdminProvisioningPort() {
        return new TenantAdminProvisioningAdapter();
    }

    @Bean
    public UserClientServicePort userClientServicePort(final ApplicationEventPublisher eventPublisher) {
        return new UserClientServiceAdapter(eventPublisher);
    }

    @Bean
    public DepartmentCoordinatesServicePort departmentCoordinatesServicePort(
            @Qualifier("voronoiCoordinatesService") final VoronoiCoordinatesService coordinatesService) {
        return new DepartmentCoordinatesServiceAdapter(coordinatesService);
    }
}
