package com.warehouse.department.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.port.primary.DepartmentPortImpl;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.domain.service.DepartmentService;
import com.warehouse.department.domain.service.DepartmentServiceImpl;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentRepositoryImpl;

@Configuration
public class DepartmentConfiguration {


    @Bean(name = "department.departmentRepository")
    public DepartmentRepository depotRepository(final DepartmentReadRepository repository) {
        return new DepartmentRepositoryImpl(repository);
    }
    @Bean
    public DepartmentPort depotPort(final DepartmentRepository departmentRepository, final DepartmentService departmentService) {
        return new DepartmentPortImpl(departmentRepository, departmentService);
    }

    @Bean
    public DepartmentService departmentService(final DepartmentRepository departmentRepository) {
        return new DepartmentServiceImpl(departmentRepository);
    }
}
