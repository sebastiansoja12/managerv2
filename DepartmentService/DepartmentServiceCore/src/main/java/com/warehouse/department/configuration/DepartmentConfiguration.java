package com.warehouse.department.configuration;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.port.primary.DepartmentPortImpl;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentRepositoryImpl;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentConfiguration {


    @Bean(name = "department.departmentRepository")
    public DepartmentRepository depotRepository(final DepartmentReadRepository repository) {
        final DepotMapper depotMapper = Mappers.getMapper(DepotMapper.class);
        return new DepartmentRepositoryImpl(repository, depotMapper);
    }
    @Bean
    public DepartmentPort depotPort(final DepartmentRepository departmentRepository) {
        return new DepartmentPortImpl(departmentRepository);
    }
}
