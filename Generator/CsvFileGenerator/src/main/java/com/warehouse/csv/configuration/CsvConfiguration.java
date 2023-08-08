package com.warehouse.csv.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.csv.domain.port.primary.CsvPort;
import com.warehouse.csv.domain.port.primary.CsvPortImpl;
import com.warehouse.csv.domain.service.CsvExporterService;
import com.warehouse.csv.domain.service.CsvExporterServiceImpl;
import com.warehouse.csv.domain.service.mapper.ParcelMapper;
import com.warehouse.shipment.domain.port.primary.ShipmentRestPort;

@Configuration
public class CsvConfiguration {

    @Bean
    public CsvPort csvPort(ShipmentRestPort shipmentRestPort, CsvExporterService exporterService) {
        return new CsvPortImpl(shipmentRestPort, exporterService);
    }

    @Bean
    public CsvExporterService exporterService() {
        final ParcelMapper parcelMapper = Mappers.getMapper(ParcelMapper.class);
        return new CsvExporterServiceImpl(parcelMapper);
    }
}
