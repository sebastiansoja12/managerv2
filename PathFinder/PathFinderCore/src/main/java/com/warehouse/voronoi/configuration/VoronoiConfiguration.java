package com.warehouse.voronoi.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.VoronoiCoordinatesService;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import com.warehouse.voronoi.domain.service.UrlReaderServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackReadRepository;
import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackRepositoryImpl;
import com.warehouse.voronoi.infrastructure.adapter.secondary.VoronoiAdapter;

@Configuration
public class VoronoiConfiguration {


    @Bean
    @Qualifier("voronoiService")
    public VoronoiService voronoiService(final VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

	@Bean
	public VoronoiPort voronoiPort(final ComputeService computeService,
                                   final VoronoiServicePort voronoiServicePort) {
		return new VoronoiPortImpl(computeService, voronoiServicePort);
	}

	@Bean
	public VoronoiServicePort voronoiServicePort(final PositionStackProperties positionStackProperties,
                                                 final PositionStackRepository positionStackRepository) {
		return new VoronoiAdapter(positionStackProperties, positionStackRepository);
	}

    @Bean
    public PositionStackRepository positionStackRepository(final PositionStackReadRepository repository) {
        return new PositionStackRepositoryImpl(repository);
    }

    @Bean
    public UrlJsonReaderService urlJsonReaderService() {
        return new UrlReaderServiceImpl();
    }

    @Bean
    @Qualifier("voronoiCoordinatesService")
    public VoronoiCoordinatesService voronoiCoordinatesService(final VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

    @Bean
    public ComputeService computeService(final VoronoiServicePort voronoiServicePort) {
        return new ComputeServiceImpl(voronoiServicePort);
    }
}
