package com.warehouse.qrcode.configuration;

import java.awt.image.BufferedImage;

import com.warehouse.qrcode.domain.port.primary.QrCodePort;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import com.warehouse.qrcode.domain.service.*;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ParcelRepositoryImpl;

@Configuration
public class BarcodeGeneratorConfiguration {

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public ParcelExportService parcelExportService(ParcelRepository parcelRepository) {
        return new ParcelExportServiceImpl(parcelRepository);
    }

    @Bean
    public ParcelRepository parcelRepository(
            @Qualifier("qrCode.parcelReadRepository") ParcelReadRepository parcelReadRepository) {
        return new ParcelRepositoryImpl(parcelReadRepository);
    }

    @Bean
    public BarcodeGeneratorService barcodeGeneratorService() {
        return new BarcodeGeneratorServiceImpl();
    }

	@Bean(name = "qrCode.parcelService")
	public QrCodePort parcelService(ParcelExportService parcelExportService) {
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
		return new QrCodePortImpl(parcelExportService, barcodeGeneratorService);
	}
}