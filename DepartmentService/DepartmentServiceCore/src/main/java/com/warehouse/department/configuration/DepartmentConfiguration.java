package com.warehouse.department.configuration;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.department.domain.port.primary.DepartmentPortImpl;
import com.warehouse.department.domain.port.secondary.DepotRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepotReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.DepotRepositoryImpl;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepartmentConfiguration {


    @Bean(name = "department.departmentRepository")
    public DepotRepository depotRepository(DepotReadRepository repository) {
        final DepotMapper depotMapper = Mappers.getMapper(DepotMapper.class);
        return new DepotRepositoryImpl(repository, depotMapper);
    }
    @Bean
    public DepartmentPort depotPort(DepotRepository depotRepository) {
        return new DepartmentPortImpl(depotRepository);
    }
}
