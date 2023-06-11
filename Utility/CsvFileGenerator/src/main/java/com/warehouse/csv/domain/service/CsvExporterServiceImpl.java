package com.warehouse.csv.domain.service;

import com.warehouse.csv.domain.model.ParcelCsv;
import com.warehouse.csv.domain.service.mapper.ParcelMapper;
import com.warehouse.shipment.domain.model.Parcel;
import lombok.AllArgsConstructor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class CsvExporterServiceImpl implements CsvExporterService {

    private final ParcelMapper parcelMapper;
    @Override
    public void exportToCSV(HttpServletResponse response, Parcel parcel) throws IOException {

        final CsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

        final ParcelCsv parcelCsv = parcelMapper.map(parcel);

        final String[] csvHeader = {"Kod paczki", "Imie nadawcy", "Nazwisko nadawcy", "Numer tel nadawcy",
                "Imie odbiorcy", "Nazwisko odbiorcy", "Numer tel odbiorcy", "Miasto", "Ulica", "Email"};
        final String[] nameMapping = {
                "id",
                "senderFirstName",
                "senderLastName",
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
