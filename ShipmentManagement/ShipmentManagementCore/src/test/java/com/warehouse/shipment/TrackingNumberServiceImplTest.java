package com.warehouse.shipment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.domain.enumeration.CarrierOperator;
import com.warehouse.shipment.domain.generator.TrackingNumberGenerator;
import com.warehouse.shipment.domain.service.TrackingNumberServiceImpl;

@ExtendWith(MockitoExtension.class)
class TrackingNumberServiceImplTest {

    @Mock
    private TrackingNumberGenerator dhlGenerator;

    @Mock
    private TrackingNumberGenerator dpdGenerator;

    @Test
    void shouldGenerateTrackingNumberWithMatchingGenerator() {
        final TrackingNumberServiceImpl service =
                new TrackingNumberServiceImpl(Set.of(dhlGenerator, dpdGenerator));
        final TrackingNumber trackingNumber = new TrackingNumber("DHL-123");
        when(dhlGenerator.canHandle(CarrierOperator.DHL)).thenReturn(true);
        when(dhlGenerator.generate()).thenReturn(trackingNumber);

        final TrackingNumber generatedTrackingNumber = service.nextTrackingNumber(CarrierOperator.DHL);

        assertEquals(trackingNumber, generatedTrackingNumber);
        verify(dhlGenerator).generate();
        verify(dpdGenerator, never()).generate();
    }

    @Test
    void shouldThrowExceptionWhenGeneratorDoesNotExistForCarrier() {
        final TrackingNumberServiceImpl service = new TrackingNumberServiceImpl(Set.of(dhlGenerator));
        when(dhlGenerator.canHandle(CarrierOperator.UPS)).thenReturn(false);

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.nextTrackingNumber(CarrierOperator.UPS));

        assertEquals("No tracking number generator for carrier: UPS", exception.getMessage());
    }
}
