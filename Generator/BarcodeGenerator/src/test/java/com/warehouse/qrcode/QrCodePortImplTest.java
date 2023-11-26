package com.warehouse.qrcode;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ParcelExportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class QrCodePortImplTest {

    @Mock
    private ParcelRepository parcelRepository;

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
        final MockHttpServletResponse response = new MockHttpServletResponse();

        doReturn(parcel)
                .when(parcelRepository)
                .find(parcelId);

        // when
        qrCodePort.exportParcelToPdfById(response, parcelId);
        // then
        verify(parcelRepository, atLeast(1)).find(parcelId);
    }

    private Parcel buildParcel() {
        return Parcel.builder()
                .id(1L)
                .build();
    }
}
