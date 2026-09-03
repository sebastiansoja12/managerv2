package com.warehouse.qrcode.domain.port.primary;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.lowagie.text.Image;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.ShipmentExportService;
import com.warehouse.qrcode.domain.vo.ShipmentId;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class QrCodePortImpl implements QrCodePort {

    private final ShipmentExportService shipmentExportService;

    private final BarcodeGeneratorService generatorService;

    private final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

    private final String headerKey = "Content-Disposition";

    private final String headerValue = "attachment; filename=%s";


    @Override
    public void exportShipment(final HttpServletResponse response, final ShipmentId shipmentId) throws Exception {
        log.info("Request label generate has been recorded for shipment: {}", shipmentId.getValue());
        response.setContentType("application/pdf");
        final String currentDateTime = dateFormatter.format(new java.util.Date());
        final String fileName = shipmentId.getValue() + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, String.format(headerValue, fileName));
        final Image image = generatorService.generateQRCodeImage(shipmentId.getValue());
        shipmentExportService.exportToPdf(response, shipmentId, image);
        log.info("Label has been successfully generated with name: {}", fileName);
    }
}
