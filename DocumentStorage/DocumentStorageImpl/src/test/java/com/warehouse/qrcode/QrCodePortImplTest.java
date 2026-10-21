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
import com.warehouse.qrcode.domain.port.secondary.DeliveryServicePort;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.ShipmentExportServiceImpl;
import com.warehouse.qrcode.domain.vo.ShipmentId;

@ExtendWith(MockitoExtension.class)
public class QrCodePortImplTest {

    @Mock
    private ShipmentServicePort shipmentServicePort;

    @Mock
    private DeliveryServicePort deliveryServicePort;

    private QrCodePortImpl qrCodePort;

    @BeforeEach
    void setup() {
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        final ShipmentExportServiceImpl parcelExportService = new ShipmentExportServiceImpl(shipmentServicePort,
                deliveryServicePort, barcodeGeneratorService);
        qrCodePort = new QrCodePortImpl(parcelExportService);
    }

    @Test
    void shouldExportQrCode() throws Exception {
        final ShipmentId shipmentId = new ShipmentId(1L);
        final Shipment shipment = buildShipment();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        doReturn(shipment)
                .when(shipmentServicePort)
                .find(shipmentId);
        doReturn("KT1")
                .when(deliveryServicePort)
                .findDestinationDepartmentCode(shipmentId);

        qrCodePort.exportShipment(response, shipmentId);
        verify(shipmentServicePort, atLeast(1)).find(shipmentId);
        verify(deliveryServicePort).findDestinationDepartmentCode(shipmentId);
    }

    private Shipment buildShipment() {
        return Shipment.builder()
                .id(1L)
                .trackingNumber("INP000000001")
                .build();
    }
}
