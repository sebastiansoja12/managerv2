package com.warehouse.exceptioncatcher.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.exceptioncatcher.domain.port.primary.ExceptionCatcherPort;
import com.warehouse.exceptioncatcher.domain.port.primary.ExceptionCatcherPortImpl;
import com.warehouse.exceptioncatcher.domain.port.secondary.ExceptionRepository;
import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.ExceptionReadRepository;
import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.ExceptionRepositoryImpl;

@Configuration
public class ExceptionCatcherConfiguration {


    @Bean
    public ExceptionCatcherPort exceptionCatcherPort(ExceptionRepository exceptionRepository) {
        return new ExceptionCatcherPortImpl(exceptionRepository);
    }

    @Bean
    public ExceptionRepository exceptionRepository(ExceptionReadRepository exceptionReadRepository) {
        return new ExceptionRepositoryImpl(exceptionReadRepository);
    }

}
