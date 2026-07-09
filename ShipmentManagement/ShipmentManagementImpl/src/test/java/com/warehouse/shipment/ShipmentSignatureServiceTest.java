package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.shipment;
import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.port.secondary.ShipmentRepository;
import com.warehouse.shipment.domain.service.ShipmentSignatureService;

@ExtendWith(MockitoExtension.class)
class ShipmentSignatureServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @Test
    void shouldUpdateShipmentSignature() {
        final ShipmentSignatureService service = new ShipmentSignatureService(shipmentRepository);
        final Shipment shipment = shipment();
        final Signature signature = new Signature("John Smith", Instant.now(), SignatureMethod.HANDWRITTEN,
                "document-reference", shipmentId(), new byte[] {1, 2, 3});
        when(shipmentRepository.findById(shipmentId())).thenReturn(shipment);

        service.updateSignature(signature);

        assertEquals(signature, shipment.getSignature());
        verify(shipmentRepository).createOrUpdate(shipment);
    }
}
