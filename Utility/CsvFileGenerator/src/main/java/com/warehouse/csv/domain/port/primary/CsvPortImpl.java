package com.warehouse.csv.domain.port.primary;

import com.itextpdf.text.DocumentException;
import com.warehouse.csv.domain.service.CsvExporterService;
import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
public class CsvPortImpl implements CsvPort {

    private final ShipmentPort shipmentPort;

    private final CsvExporterService exporterService;

    @Override
    public void exportToCSV(HttpServletResponse response, Long id) throws DocumentException, IOException {
        final Parcel parcel = shipmentPort.loadParcel(id);
        response.setContentType("text/csv");
        final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        final String currentDateTime = dateFormatter.format(new Date());
        final String headerKey = "Content-Disposition";
        final String headerValue = "attachment; filename=parcel_id" + parcel.getId() + "_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);
        exporterService.exportToCSV(response, parcel);
    }
}
