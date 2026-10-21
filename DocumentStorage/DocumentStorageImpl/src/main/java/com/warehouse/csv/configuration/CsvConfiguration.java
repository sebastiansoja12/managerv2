package com.warehouse.csv.configuration;

import com.warehouse.csv.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.csv.domain.port.primary.CsvPort;
import com.warehouse.csv.domain.port.primary.CsvPortImpl;
import com.warehouse.csv.domain.port.secondary.ShipmentServicePort;
import com.warehouse.csv.domain.service.CsvExporterService;
import com.warehouse.csv.domain.service.CsvExporterServiceImpl;

@Configuration
public class CsvConfiguration {

    @Bean
    public CsvPort csvPort(CsvExporterService exporterService) {
        return new CsvPortImpl(exporterService);
    }

    @Bean
    public CsvExporterService exporterService(ShipmentServicePort shipmentServicePort) {
        return new CsvExporterServiceImpl(shipmentServicePort);
    }

    @Bean("csv.shipmentServicePort")
    public ShipmentServicePort csvShipmentServicePort(ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }
}
