package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.shipmentId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.context.ShipmentEventContext;
import com.warehouse.shipment.domain.event.SignatureSigned;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.port.secondary.SignatureRepository;
import com.warehouse.shipment.application.service.SignatureServiceImpl;

@ExtendWith(MockitoExtension.class)
class SignatureServiceImplTest {

    @Mock
    private SignatureRepository signatureRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        new ShipmentEventContext().setApplicationEventPublisher(eventPublisher);
    }

    @Test
    void shouldCreateSignature() {
        final SignatureServiceImpl service = new SignatureServiceImpl(signatureRepository, shipmentRepository);
        final Signature signature = new Signature("John Smith", Instant.now(), SignatureMethod.DIGITAL,
                "document-reference", shipmentId(), new byte[] {1, 2, 3});

        service.createSignature(signature);

        verify(signatureRepository).save(signature);
        verify(eventPublisher).publishEvent(any(SignatureSigned.class));
    }
}
