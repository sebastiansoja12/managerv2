package com.warehouse.qrcode.configuration;

import java.awt.image.BufferedImage;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.qrcode.domain.port.primary.QrCodePort;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.LabelConfigurationServicePort;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.PdfShipmentLabelGenerator;
import com.warehouse.qrcode.domain.service.ShipmentLabelGeneratorFactory;
import com.warehouse.qrcode.domain.service.ZplShipmentLabelGenerator;
import com.warehouse.qrcode.infrastructure.adapter.secondary.LabelConfigurationServiceAdapter;
import com.warehouse.qrcode.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.shipment.infrastructure.ShipmentApiService;

@Configuration
public class BarcodeGeneratorConfiguration {

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public ShipmentServicePort qrCodeShipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public LabelConfigurationServicePort labelConfigurationServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService) {
        return new LabelConfigurationServiceAdapter(operatorConfigurationApiService);
    }

    @Bean
    public BarcodeGeneratorService barcodeGeneratorService() {
        return new BarcodeGeneratorServiceImpl();
    }

    @Bean
    public ShipmentLabelGeneratorFactory shipmentLabelGeneratorFactory(
            final BarcodeGeneratorService barcodeGeneratorService) {
        return new ShipmentLabelGeneratorFactory(List.of(
                PdfShipmentLabelGenerator.a6(barcodeGeneratorService),
                PdfShipmentLabelGenerator.a4(barcodeGeneratorService),
                new ZplShipmentLabelGenerator()));
    }

    @Bean
    public QrCodePort parcelService(final ShipmentServicePort shipmentServicePort,
                                    final LabelConfigurationServicePort labelConfigurationServicePort,
                                    final ShipmentLabelGeneratorFactory shipmentLabelGeneratorFactory) {
        return new QrCodePortImpl(shipmentServicePort, labelConfigurationServicePort, shipmentLabelGeneratorFactory);
    }
}
