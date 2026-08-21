package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;
import com.warehouse.shipment.application.service.SignatureServiceImpl;

@ExtendWith(MockitoExtension.class)
class SignatureServiceImplTest {

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private ShipmentReadModelRepository shipmentReadModelRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private DomainEventPublisher domainEventPublisher;

    @Test
    void shouldCreateSignature() {
        final SignatureServiceImpl service = new SignatureServiceImpl(
                signatureRepository, shipmentRepository, shipmentReadModelRepository, domainEventPublisher);
        final Signature signature = new Signature("John Smith", Instant.now(), SignatureMethod.DIGITAL,
                "document-reference", shipmentId(), new byte[] {1, 2, 3});
        when(shipmentRepository.findById(shipmentId())).thenReturn(DataTestCreator.shipment());
        service.createSignature(signature);

        assertNotNull(signature.getSignatureId());
        verify(shipmentRepository).findById(shipmentId());
        verify(signatureRepository).save(signature);
        verify(shipmentReadModelRepository).sync(any(ShipmentSnapshot.class));
        verify(domainEventPublisher).publish(any(SignatureSigned.class));
    }
}
