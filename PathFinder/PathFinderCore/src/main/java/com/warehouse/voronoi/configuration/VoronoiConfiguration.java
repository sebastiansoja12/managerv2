package com.warehouse.voronoi.configuration;

import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.infrastructure.GeocodingApiService;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.VoronoiCoordinatesService;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.GeocodingConfigServicePort;
import com.warehouse.voronoi.domain.port.secondary.GeolocationServiceProvider;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import com.warehouse.voronoi.domain.service.UrlReaderServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.GeocodingConfigServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.GeolocationAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackReadRepository;
import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackRepositoryImpl;

@Configuration
public class VoronoiConfiguration {


    @Bean
    @Qualifier("voronoiService")
    public VoronoiService voronoiService(final VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

	@Bean
	public VoronoiPort voronoiPort(final ComputeService computeService,
                                   final Set<GeolocationServiceProvider> geolocationServiceProvider,
                                   final GeocodingConfigServicePort configServicePort) {
		return new VoronoiPortImpl(computeService, geolocationServiceProvider, configServicePort);
	}

    @Bean
    public GeocodingConfigServicePort geocodingConfigServicePort(final GeocodingApiService geocodingApiService) {
        return new GeocodingConfigServiceAdapter(geocodingApiService);
    }

	@Bean
	public GeolocationServiceProvider voronoiServicePort(final PositionStackProperties positionStackProperties,
                                                         final PositionStackRepository positionStackRepository) {
		return new GeolocationAdapter(positionStackProperties, positionStackRepository);
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
    public ComputeService computeService(final Set<GeolocationServiceProvider> geolocationServiceProvider,
                                         final GeocodingConfigServicePort geocodingConfigServicePort) {
        return new ComputeServiceImpl(geolocationServiceProvider, geocodingConfigServicePort);
    }
}
