package com.warehouse.tsp.configuration;

import com.warehouse.tsp.domain.port.primary.TravellingSalesManPort;
import com.warehouse.tsp.domain.port.primary.TravellingSalesManPortImpl;
import com.warehouse.tsp.domain.port.secondary.SalesManServicePort;
import com.warehouse.tsp.domain.service.*;
import com.warehouse.tsp.infrastructure.adapter.secondary.TSPAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TSPConfiguration {

    @Bean
    public TravellingSalesManPort travellingSalesManPort(SalesManServicePort salesManServicePort) {
        return new TravellingSalesManPortImpl(salesManServicePort);
    }

    @Bean
    public SalesManServicePort salesManServicePort() {
        final CalculateDistanceBetweenDepots calculateDistanceBetweenDepots =
                new CalculateDistanceBetweenDepotsServiceImpl();
        final CalculateDistanceService calculateDistanceService =
                new CalculateDistanceServiceImpl(calculateDistanceBetweenDepots);
        final RandomElementsSwapper randomElementsSwapper = new RandomElementsSwapperImpl();
        final ComputeService computeService = new ComputeServiceImpl(calculateDistanceService, randomElementsSwapper);
        return new TSPAdapter(computeService);
    }
}
