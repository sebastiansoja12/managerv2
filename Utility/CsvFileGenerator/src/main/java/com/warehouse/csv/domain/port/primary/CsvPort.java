package com.warehouse.csv.domain.port.primary;

import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface CsvPort {

    void exportToCSV(HttpServletResponse response, Long id) throws DocumentException, IOException;
}
