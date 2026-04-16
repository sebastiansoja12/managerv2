package com.warehouse.shipment;

import static com.warehouse.shipment.DataTestCreator.lockedShipment;
import static com.warehouse.shipment.DataTestCreator.shipment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.warehouse.exceptionhandler.exception.RestException;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.service.ShipmentStateValidatorServiceImpl;

class ShipmentStateValidatorServiceImplTest {

    @Test
    void shouldReturnSuccessWhenShipmentIsNotLocked() {
        final ShipmentStateValidatorServiceImpl service = new ShipmentStateValidatorServiceImpl();

        final Result<Void, String> result = service.validateShipmentState(shipment());

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldThrowExceptionWhenShipmentIsLocked() {
        final ShipmentStateValidatorServiceImpl service = new ShipmentStateValidatorServiceImpl();

        final RestException exception = assertThrows(RestException.class,
                () -> service.validateShipmentState(lockedShipment()));

        assertEquals("Cannot change shipment type when it is locked", exception.getMessage());
    }
}
