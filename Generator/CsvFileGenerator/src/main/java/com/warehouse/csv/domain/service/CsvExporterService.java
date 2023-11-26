package com.warehouse.csv.domain.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

public interface CsvExporterService {

    void exportToCSV(HttpServletResponse response, Long id) throws DocumentException, IOException;
}
