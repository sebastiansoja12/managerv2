package com.warehouse.parcelstatuschange.configuration;

import com.warehouse.parcelstatuschange.domain.port.primary.ParcelStatusPort;
import com.warehouse.parcelstatuschange.domain.port.primary.ParcelStatusPortImpl;
import com.warehouse.parcelstatuschange.domain.port.secondary.ParcelStatusRepository;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.ParcelStatusReadRepository;
import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.ParcelStatusRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParcelStatusChangeConfiguration {

    @Bean
    public ParcelStatusPort parcelStatusPort(ParcelStatusRepository parcelStatusRepository) {
        return new ParcelStatusPortImpl(parcelStatusRepository);
    }

    @Bean
    public ParcelStatusRepository parcelStatusRepository(ParcelStatusReadRepository parcelStatusReadRepository) {
        return new ParcelStatusRepositoryImpl(parcelStatusReadRepository);
    }
}
