package com.warehouse.voronoi.configuration;

import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import com.warehouse.voronoi.domain.service.UrlReaderServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.primary.mapper.AddressRequestMapper;
import com.warehouse.voronoi.infrastructure.adapter.secondary.VoronoiAdapter;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoronoiConfiguration {

    @Bean
    public VoronoiService addressDeterminationService(VoronoiPort voronoiPort) {
        final AddressRequestMapper addressRequestMapper = Mappers.getMapper(AddressRequestMapper.class);
        return new VoronoiServiceAdapter(voronoiPort, addressRequestMapper);
    }

    @Bean
    public VoronoiPort addressDeterminationPort(VoronoiServicePort determinationServicePort) {
        return new VoronoiPortImpl(determinationServicePort);
    }

    @Bean
    public VoronoiServicePort addressDeterminationServicePort(ComputeService computeService,
          UrlJsonReaderService urlJsonReaderService, PositionStackProperties positionStackProperties) {
        return new VoronoiAdapter(positionStackProperties, computeService, urlJsonReaderService);
    }

    @Bean
    public UrlJsonReaderService urlJsonReaderService() {
        return new UrlReaderServiceImpl();
    }

    @Bean
    public ComputeService computeService() {
        return new ComputeServiceImpl();
    }
}
