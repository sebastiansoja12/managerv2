package com.warehouse.qrcode.configuration;

import java.awt.image.BufferedImage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.warehouse.logisticsdelivery.api.DeliveryApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.qrcode.domain.port.primary.QrCodePort;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.DeliveryServicePort;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ShipmentExportService;
import com.warehouse.qrcode.domain.service.ShipmentExportServiceImpl;
import com.warehouse.qrcode.infrastructure.adapter.secondary.DeliveryServiceAdapter;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

@Configuration
public class BarcodeGeneratorConfiguration {

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public ShipmentExportService parcelExportService(ShipmentServicePort shipmentServicePort,
                                                     DeliveryServicePort deliveryServicePort,
                                                     BarcodeGeneratorService barcodeGeneratorService) {
        return new ShipmentExportServiceImpl(shipmentServicePort, deliveryServicePort, barcodeGeneratorService);
    }

    @Bean
    public ShipmentServicePort qrCodeShipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public BarcodeGeneratorService barcodeGeneratorService() {
        return new BarcodeGeneratorServiceImpl();
    }

	@Bean
	public QrCodePort parcelService(final ShipmentExportService shipmentExportService) {
		return new QrCodePortImpl(shipmentExportService);
	}

    @Bean
    public ShipmentExportService shipmentExportService(final ShipmentServicePort shipmentServicePort,
                                                       final DeliveryServicePort deliveryServicePort,
                                                       final BarcodeGeneratorService barcodeGeneratorService) {
        return new ShipmentExportServiceImpl(shipmentServicePort, deliveryServicePort, barcodeGeneratorService);
    }

    @Bean
    public DeliveryServicePort qrCodeDeliveryServicePort(final DeliveryApiService deliveryApiService,
                                                         final DepartmentApiService departmentApiService) {
        return new DeliveryServiceAdapter(deliveryApiService, departmentApiService);
    }
}