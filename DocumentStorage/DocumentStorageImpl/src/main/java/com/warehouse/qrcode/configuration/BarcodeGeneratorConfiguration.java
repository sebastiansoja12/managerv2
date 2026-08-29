package com.warehouse.qrcode.configuration;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.warehouse.qrcode.domain.port.primary.QrCodePort;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.ShipmentRepository;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ShipmentExportService;
import com.warehouse.qrcode.domain.service.ShipmentExportServiceImpl;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentRepositoryImpl;

@Configuration
public class BarcodeGeneratorConfiguration {

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public ShipmentExportService parcelExportService(ShipmentRepository shipmentRepository) {
        return new ShipmentExportServiceImpl(shipmentRepository);
    }

    @Bean
    public ShipmentRepository parcelRepository(
            @Qualifier("qrCode.shipmentReadRepository") ShipmentReadRepository shipmentReadRepository) {
        return new ShipmentRepositoryImpl(shipmentReadRepository);
    }

    @Bean
    public BarcodeGeneratorService barcodeGeneratorService() {
        return new BarcodeGeneratorServiceImpl();
    }

	@Bean
	public QrCodePort parcelService(final ShipmentExportService shipmentExportService) {
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
		return new QrCodePortImpl(shipmentExportService, barcodeGeneratorService);
	}

    @Bean
    public ShipmentExportService shipmentExportService(final ShipmentRepository shipmentRepository) {
        return new ShipmentExportServiceImpl(shipmentRepository);
    }
}