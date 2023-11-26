package com.warehouse.csv;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.itextpdf.text.DocumentException;
import com.warehouse.csv.domain.port.primary.CsvPortImpl;
import com.warehouse.csv.domain.port.secondary.ParcelRepository;
import com.warehouse.csv.domain.service.CsvExporterService;
import com.warehouse.csv.domain.service.CsvExporterServiceImpl;
import com.warehouse.csv.domain.vo.ParcelCsv;

@ExtendWith(MockitoExtension.class)
public class CsvPortImplTest {


    @Mock
    private ParcelRepository parcelRepository;

    private CsvPortImpl csvPort;

    @BeforeEach
    void setup() {
        final CsvExporterService csvExporterService = new CsvExporterServiceImpl(parcelRepository);
        csvPort = new CsvPortImpl(csvExporterService);
    }

    @Test
    void shouldGenerateCSVFile() throws DocumentException, IOException {
        // given
        final MockHttpServletResponse response = new MockHttpServletResponse();
        final Long parcelId = 1L;
        when(parcelRepository.find(parcelId)).thenReturn(buildParcel());
        // when
        csvPort.exportToCSV(response, parcelId);
        // then
        verify(parcelRepository, atLeast(1)).find(parcelId);
    }

    private ParcelCsv buildParcel() {
        return ParcelCsv.builder()
                .id(1L)
                .build();
    }
}
