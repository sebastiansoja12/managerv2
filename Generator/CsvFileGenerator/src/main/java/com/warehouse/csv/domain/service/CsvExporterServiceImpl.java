package com.warehouse.csv.domain.service;

import java.io.IOException;

import com.warehouse.csv.domain.port.secondary.ParcelRepository;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.warehouse.csv.domain.vo.ParcelCsv;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CsvExporterServiceImpl implements CsvExporterService {

    private final ParcelRepository parcelRepository;

    @Override
    public void exportToCSV(HttpServletResponse response, Long id) throws IOException {

        final CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        final ParcelCsv parcelCsv = parcelRepository.find(id);

        final String[] csvHeader = {"Kod paczki", "Imie nadawcy", "Nazwisko nadawcy", "Numer tel nadawcy",
                "Imie odbiorcy", "Nazwisko odbiorcy", "Numer tel odbiorcy", "Miasto", "Ulica", "Email"};

        final String[] nameMapping = {
                "id",
                "firstName",
                "lastName",
                "senderTelephoneNumber",
                "recipientFirstName",
                "recipientLastName",
                "recipientTelephoneNumber",
                "recipientCity",
                "recipientStreet",
                "recipientEmail"
        };

        csvWriter.writeHeader(csvHeader);
        csvWriter.write(parcelCsv, nameMapping);

        csvWriter.close();

    }
}
