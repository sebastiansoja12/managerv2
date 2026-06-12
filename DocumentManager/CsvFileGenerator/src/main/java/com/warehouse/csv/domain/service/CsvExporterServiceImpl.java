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
                "Imie odbiorcy", "Nazwisko odbiorcy", "Numer tel odbiorcy", "Miasto", "Ulica", "Email",
                "Numer UN", "Prawidlowa nazwa przewozowa", "Klasa zagrozenia", "Grupa pakowania",
                "Ilosc", "Jednostka", "Liczba opakowan", "Typ opakowania", "Regulacja", "Typ transportu",
                "Kontakt alarmowy 24h", "Ilosc ograniczona", "Zanieczyszczenie morskie", "Korozyjny"};

        final String[] nameMapping = {
                "shipmentId",
                "firstName",
                "lastName",
                "senderTelephoneNumber",
                "recipientFirstName",
                "recipientLastName",
                "recipientTelephoneNumber",
                "recipientCity",
                "recipientStreet",
                "recipientEmail",
                "dangerousGoodUnNumber",
                "dangerousGoodProperShippingName",
                "dangerousGoodHazardClass",
                "dangerousGoodPackingGroup",
                "dangerousGoodQuantity",
                "dangerousGoodQuantityUnit",
                "dangerousGoodPackageCount",
                "dangerousGoodPackagingType",
                "dangerousGoodRegulationType",
                "dangerousGoodTransportMode",
                "dangerousGoodEmergencyContact24h",
                "dangerousGoodLimitedQuantity",
                "dangerousGoodMarinePollutant",
                "dangerousGoodCorrosive"
        };

        csvWriter.writeHeader(csvHeader);
        csvWriter.write(parcelCsv, nameMapping);

        csvWriter.close();

    }
}
