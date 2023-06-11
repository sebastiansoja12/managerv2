package com.warehouse.csv.infrastructure.adapter.primary;

import com.itextpdf.text.DocumentException;
import com.warehouse.csv.domain.port.primary.CsvPort;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/csv")
@AllArgsConstructor
public class CsvController {

    private final CsvPort csvPort;

    @GetMapping("/{parcelId}")
    public void generateLabel(HttpServletResponse response, @PathVariable Long parcelId) throws DocumentException, IOException {
        csvPort.exportToCSV(response, parcelId);
    }
}
