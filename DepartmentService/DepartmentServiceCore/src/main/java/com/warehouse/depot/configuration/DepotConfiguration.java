package com.warehouse.depot.configuration;

import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.domain.port.primary.DepotPortImpl;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.infrastructure.adapter.secondary.DepotReadRepository;
import com.warehouse.depot.infrastructure.adapter.secondary.DepotRepositoryImpl;
import com.warehouse.depot.infrastructure.adapter.secondary.mapper.DepotMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DepotConfiguration {


    @Bean(name = "depotDepotRepository")
    public DepotRepository depotRepository(DepotReadRepository repository) {
        final DepotMapper depotMapper = Mappers.getMapper(DepotMapper.class);
        return new DepotRepositoryImpl(repository, depotMapper);
    }
    @Bean
    public DepotPort depotPort(DepotRepository depotRepository) {
        return new DepotPortImpl(depotRepository);
    }
}
