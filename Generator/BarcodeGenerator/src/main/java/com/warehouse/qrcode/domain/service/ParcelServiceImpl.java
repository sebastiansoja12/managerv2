package com.warehouse.qrcode.domain.service;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.primary.ShipmentRestPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final ShipmentRestPort shipmentRestPort;

    private final ParcelExportService parcelExportService;

    @Override
    public void exportParcelToPdfById(HttpServletResponse response, Long id) throws Exception {
        log.info("Request label generate has been recorded for parcel: {}", id);
        final Parcel parcel = shipmentRestPort.loadParcel(id);
        response.setContentType("application/pdf");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        final String currentDateTime = dateFormatter.format(new java.util.Date());
        final String headerKey = "Content-Disposition";
        final String fileName = parcel.getId() + "_" + currentDateTime + ".pdf";
        final String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        parcelExportService.exportToPdf(response, parcel);
        log.info("Label has been successfully generated with name: {}", fileName);

    }
}
