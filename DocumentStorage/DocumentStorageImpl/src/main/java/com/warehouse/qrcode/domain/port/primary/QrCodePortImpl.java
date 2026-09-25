package com.warehouse.qrcode.domain.port.primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.LabelConfigurationServicePort;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.service.ShipmentLabelGenerator;
import com.warehouse.qrcode.domain.service.ShipmentLabelGeneratorFactory;
import com.warehouse.qrcode.domain.vo.ShipmentId;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QrCodePortImpl implements QrCodePort {

    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private final ShipmentServicePort shipmentServicePort;

    private final LabelConfigurationServicePort labelConfigurationServicePort;

    private final ShipmentLabelGeneratorFactory shipmentLabelGeneratorFactory;

    public QrCodePortImpl(final ShipmentServicePort shipmentServicePort,
                          final LabelConfigurationServicePort labelConfigurationServicePort,
                          final ShipmentLabelGeneratorFactory shipmentLabelGeneratorFactory) {
        this.shipmentServicePort = shipmentServicePort;
        this.labelConfigurationServicePort = labelConfigurationServicePort;
        this.shipmentLabelGeneratorFactory = shipmentLabelGeneratorFactory;
    }

    @Override
    public void exportShipment(final HttpServletResponse response, final ShipmentId shipmentId) throws Exception {
        log.info("Request label generate has been recorded for shipment: {}", shipmentId.getValue());
        final ShipmentLabelGenerator generator =
                shipmentLabelGeneratorFactory.forFormat(labelConfigurationServicePort.currentLabelFormat());
        final Shipment shipment = shipmentServicePort.find(shipmentId);
        final String fileName = shipmentId.getValue() + "_" + LocalDateTime.now().format(FILE_DATE_FORMATTER)
                + "." + generator.fileExtension();
        response.setContentType(generator.contentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        generator.generate(response.getOutputStream(), shipment);
        log.info("Label {} has been successfully generated with name: {}", generator.format(), fileName);
    }
}
