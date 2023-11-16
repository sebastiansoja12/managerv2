package com.warehouse.qrcode;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ParcelExportServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
@Disabled
public class QrCodePortImplTest {


    @Mock
    private ParcelRepository parcelRepository;

    @Spy
    private HttpServletResponse response;

    private QrCodePortImpl qrCodePort;

    @BeforeEach
    void setup() {
        final ParcelExportServiceImpl parcelExportService = new ParcelExportServiceImpl(parcelRepository);
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        qrCodePort = new QrCodePortImpl(parcelExportService, barcodeGeneratorService);
    }

    @Test
    void shouldExportQrCode() throws Exception {
        // given
        final Long parcelId = 1L;
        final Parcel parcel = buildParcel();

        doReturn(parcel)
                .when(parcelRepository)
                .find(parcelId);

        // when
        qrCodePort.exportParcelToPdfById(response, parcelId);
        // then
        verify(response).setContentType("application/pdf");
        verify(response).setHeader(anyString(), anyString());
    }

    private Parcel buildParcel() {
        return Parcel.builder()
                .id(1L)
                .build();
    }
}
