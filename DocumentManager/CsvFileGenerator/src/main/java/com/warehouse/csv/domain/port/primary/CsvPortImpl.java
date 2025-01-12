package com.warehouse.csv.domain.port.primary;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.DocumentException;
import com.warehouse.csv.domain.service.CsvExporterService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CsvPortImpl implements CsvPort {

    private final CsvExporterService exporterService;

    private final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private final String headerKey = "Content-Disposition";

    private final String headerValue = "attachment; filename=%s_%s%s";

    private final String fileName = "%s_%s%s";

    public static final String CSV = ".csv";


    @Override
    public void exportToCSV(HttpServletResponse response, Long id) throws DocumentException, IOException {
        log.info("Request CSV file generate has been recorded for parcel: {}", id);
        response.setContentType("text/csv");
        final String currentDateTime = dateFormatter.format(new Date());
        response.setHeader(headerKey, String.format(headerValue, id, currentDateTime, CSV));
        exporterService.exportToCSV(response, id);
        log.info("CSV file has been successfully generated with name: {}", String.format(fileName, id, currentDateTime,
                CSV));
    }
}
