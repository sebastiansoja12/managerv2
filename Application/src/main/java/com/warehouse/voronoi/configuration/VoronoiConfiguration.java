package com.warehouse.voronoi.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.application.port.primary.VoronoiPort;
import com.warehouse.voronoi.application.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;

@Configuration
public class VoronoiConfiguration {

    @Bean
    @Qualifier("voronoiService")
    public VoronoiService voronoiService(final VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

    @Bean
    public VoronoiPort voronoiPort(final ComputeService computeService) {
        return new VoronoiPortImpl(computeService);
    }

    @Bean
    public ComputeService computeService() {
        return new ComputeServiceImpl();
    }
}
