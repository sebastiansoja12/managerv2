package com.warehouse.voronoi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.positionstack.PositionStackProperties;
import com.warehouse.voronoi.VoronoiService;
import com.warehouse.voronoi.domain.port.primary.VoronoiPort;
import com.warehouse.voronoi.domain.port.primary.VoronoiPortImpl;
import com.warehouse.voronoi.domain.port.secondary.DepotServicePort;
import com.warehouse.voronoi.domain.port.secondary.VoronoiServicePort;
import com.warehouse.voronoi.domain.service.ComputeService;
import com.warehouse.voronoi.domain.service.ComputeServiceImpl;
import com.warehouse.voronoi.domain.service.UrlJsonReaderService;
import com.warehouse.voronoi.domain.service.UrlReaderServiceImpl;
import com.warehouse.voronoi.infrastructure.adapter.primary.VoronoiServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.DepotServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.VoronoiAdapter;

@Configuration
public class VoronoiConfiguration {


    @Bean
    public VoronoiService addressDeterminationService(VoronoiPort voronoiPort) {
        return new VoronoiServiceAdapter(voronoiPort);
    }

	@Bean
	public VoronoiPort addressDeterminationPort(DepotServicePort depotServicePort, ComputeService computeService) {
		return new VoronoiPortImpl(depotServicePort, computeService);
	}

    @Bean
    public DepotServicePort depotServicePort(DepartmentPort departmentPort) {
        return new DepotServiceAdapter(departmentPort);
    }

	@Bean
	public VoronoiServicePort addressDeterminationServicePort(PositionStackProperties positionStackProperties) {
		return new VoronoiAdapter(positionStackProperties);
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
