package com.warehouse.process.configuration;

import com.warehouse.process.domain.port.secondary.ProcessRepository;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessLogReadRepository;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessLogWriteRepository;
import com.warehouse.process.infrastructure.adapter.secondary.ProcessRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessConfiguration {

    @Bean
    public ProcessRepository processRepository(final ProcessLogReadRepository readRepository,
                                               final ProcessLogWriteRepository writeRepository) {
        return new ProcessRepositoryImpl(readRepository, writeRepository);
    }
}
