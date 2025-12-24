package com.warehouse.voronoi.configuration;

import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackReadRepository;
import com.warehouse.voronoi.infrastructure.adapter.secondary.PositionStackRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.DepartmentServicePort;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import com.warehouse.voronoi.domain.service.UrlReaderServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.DepartmentServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.VoronoiAdapter;

@Configuration
public class VoronoiConfiguration {


    @Bean
    public VoronoiService addressDeterminationService(VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

	@Bean
	public VoronoiPort addressDeterminationPort(DepartmentServicePort departmentServicePort, ComputeService computeService) {
		return new VoronoiPortImpl(departmentServicePort, computeService);
	}

    @Bean
    public DepartmentServicePort depotServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
    }

	@Bean
	public VoronoiServicePort addressDeterminationServicePort(PositionStackProperties positionStackProperties,
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
    public ComputeService computeService(VoronoiServicePort voronoiServicePort) {
        return new ComputeServiceImpl(voronoiServicePort);
    }
}
