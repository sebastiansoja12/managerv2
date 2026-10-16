package com.warehouse.csv.configuration;

import com.warehouse.csv.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.csv.infrastructure.adapter.secondary.ParcelRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.csv.domain.port.primary.CsvPort;
import com.warehouse.csv.domain.port.primary.CsvPortImpl;
import com.warehouse.csv.domain.port.secondary.ParcelRepository;
import com.warehouse.csv.domain.service.CsvExporterService;
import com.warehouse.csv.domain.service.CsvExporterServiceImpl;

@Configuration
public class CsvConfiguration {

    @Bean
    public CsvPort csvPort(CsvExporterService exporterService) {
        return new CsvPortImpl(exporterService);
    }

    @Bean
    public CsvExporterService exporterService(ParcelRepository parcelRepository) {
        return new CsvExporterServiceImpl(parcelRepository);
    }

    @Bean("csv.parcelRepository")
    public ParcelRepository parcelRepository(ParcelReadRepository parcelReadRepository) {
        return new ParcelRepositoryImpl(parcelReadRepository);
    }
}
