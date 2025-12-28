package com.warehouse.process.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.process.ProcessHubApiService;
import com.warehouse.process.domain.port.primary.ProcessPort;
import com.warehouse.process.domain.port.primary.ProcessPortImpl;
import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.domain.service.ProcessService;
import com.warehouse.process.domain.service.ProcessServiceImpl;
import com.warehouse.process.infrastructure.adapter.primary.ProcessResourceAdapter;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessLogReadRepository;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessLogWriteRepository;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessRepositoryImpl;

@Configuration
public class ProcessConfiguration {

    @Bean
    public ProcessRepository processRepository(final ProcessLogReadRepository readRepository,
                                               final ProcessLogWriteRepository writeRepository) {
        return new ProcessRepositoryImpl(readRepository, writeRepository);
    }

    @Bean
    public ProcessPort processPort(final ProcessService processService) {
        return new ProcessPortImpl(processService);
    }

    @Bean
    public ProcessService processService(final ProcessRepository processRepository) {
        return new ProcessServiceImpl(processRepository);
    }

    @Bean
    public ProcessHubApiService processHubApiService(final ProcessPort processPort) {
        return new ProcessResourceAdapter(processPort);
    }
}
