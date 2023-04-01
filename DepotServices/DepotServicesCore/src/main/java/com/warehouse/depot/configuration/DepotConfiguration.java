package com.warehouse.depot.configuration;

import com.warehouse.depot.api.DepotService;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.domain.port.primary.DepotPortImpl;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.infrastructure.primary.DepotServiceAdapter;
import com.warehouse.depot.infrastructure.primary.mapper.DepotRequestMapper;
import com.warehouse.depot.infrastructure.primary.mapper.DepotResponseMapper;
import com.warehouse.depot.infrastructure.secondary.DepotReadRepository;
import com.warehouse.depot.infrastructure.secondary.DepotRepositoryImpl;
import com.warehouse.depot.infrastructure.secondary.mapper.DepotMapper;
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

    @Bean(name = "depotDepotService")
    public DepotService depotService(DepotPort depotPort) {
        final DepotRequestMapper requestMapper = Mappers.getMapper(DepotRequestMapper.class);
        final DepotResponseMapper responseMapper = Mappers.getMapper(DepotResponseMapper.class);
        return new DepotServiceAdapter(requestMapper, responseMapper, depotPort);
    }

    @Bean
    public DepotPort depotPort(DepotRepository depotRepository) {
        return new DepotPortImpl(depotRepository);
    }
}
