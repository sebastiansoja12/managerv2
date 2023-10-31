package com.warehouse.returning.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.returning.domain.port.primary.ReturnPort;
import com.warehouse.returning.domain.port.primary.ReturnPortImpl;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.service.ReturnService;
import com.warehouse.returning.domain.service.ReturnServiceImpl;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnInformationReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturnReadRepository;
import com.warehouse.returning.infrastructure.adapter.secondary.ReturningRepositoryImpl;

@Configuration
public class ReturningConfiguration {

    @Bean
    public ReturnPort returnPort(ReturnRepository returnRepository) {
        final ReturnService returnService = new ReturnServiceImpl(returnRepository);
        return new ReturnPortImpl(returnService);
    }

    @Bean
	public ReturnRepository returnRepository(ReturnReadRepository repository,
			ReturnInformationReadRepository returnInformationReadRepository) {
        return new ReturningRepositoryImpl(repository);
    }
}
