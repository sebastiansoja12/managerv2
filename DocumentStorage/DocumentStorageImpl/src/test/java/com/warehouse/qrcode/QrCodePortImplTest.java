package com.warehouse.qrcode;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.ShipmentRepository;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ShipmentExportServiceImpl;
import com.warehouse.qrcode.domain.vo.ShipmentId;

@ExtendWith(MockitoExtension.class)
public class QrCodePortImplTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    private QrCodePortImpl qrCodePort;

    @BeforeEach
    void setup() {
        final ShipmentExportServiceImpl parcelExportService = new ShipmentExportServiceImpl(shipmentRepository);
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        qrCodePort = new QrCodePortImpl(parcelExportService, barcodeGeneratorService);
    }

    @Test
    void shouldExportQrCode() throws Exception {
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Shipment shipment = buildShipment();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        doReturn(shipment)
                .when(shipmentRepository)
                .find(shipmentId);

        qrCodePort.exportShipment(response, shipmentId);
        verify(shipmentRepository, atLeast(1)).find(shipmentId);
    }

    private Shipment buildShipment() {
        return Shipment.builder()
                .id(1L)
                .build();
    }
}
