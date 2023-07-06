package com.warehouse.star.configuration;

import com.warehouse.star.StarService;
import com.warehouse.star.domain.port.primary.StarPort;
import com.warehouse.star.domain.port.primary.StarPortImpl;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepots;
import com.warehouse.star.domain.service.CalculateDistanceBetweenDepotsServiceImpl;
import com.warehouse.star.infrastructure.adapter.primary.StarServiceAdapter;
import com.warehouse.star.infrastructure.adapter.primary.mapper.StarRequestMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarConfiguration {

    @Bean
    public StarService starService(StarPort starPort) {
        final StarRequestMapper starRequestMapper = Mappers.getMapper(StarRequestMapper.class);
        return new StarServiceAdapter(starPort, starRequestMapper);
    }

    @Bean
    public StarPort starPort() {
        final CalculateDistanceBetweenDepots
                calculateDistanceBetweenDepots = new CalculateDistanceBetweenDepotsServiceImpl();
        return new StarPortImpl(calculateDistanceBetweenDepots);
    }
}
