package com.warehouse.csv.domain.service;

import com.itextpdf.text.DocumentException;
import com.warehouse.shipment.domain.model.Parcel;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CsvExporterService {

    void exportToCSV(HttpServletResponse response, Parcel parcel) throws DocumentException, IOException;
}
