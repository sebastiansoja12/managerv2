package com.warehouse.shipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.DangerousGoodNotFoundException;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.port.secondary.ShipmentConfigurationServicePort;
import com.warehouse.shipment.infrastructure.adapter.primary.ShipmentInternalController;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DangerousGoodApi;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.warehouse.shipment.DataTestCreator.dangerousGood;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentInternalControllerDangerousGoodTest {

    private static final long SHIPMENT_ID = 42L;

    @Mock
    private ShipmentPort shipmentPort;

    @Mock
    private ShipmentRequestValidator shipmentRequestValidator;

    @Mock
    private ShipmentRequestMapper requestMapper;

    @Mock
    private ShipmentResponseMapper responseMapper;

    @Mock
    private ShipmentConfigurationServicePort shipmentConfigurationServicePort;

    private ShipmentInternalController controller;

    @BeforeEach
    void setUp() {
        controller = new ShipmentInternalController(
                shipmentPort,
                shipmentRequestValidator,
                requestMapper,
                responseMapper,
                new ObjectMapper(),
                shipmentConfigurationServicePort
        );
    }

    @Test
    void shouldReturnDangerousGood() {
        final DangerousGood dangerousGood = dangerousGood();
        final DangerousGoodApi api = api("Rechargeable battery");
        when(shipmentPort.loadDangerousGood(any(ShipmentId.class))).thenReturn(Optional.of(dangerousGood));
        when(responseMapper.map(dangerousGood)).thenReturn(api);

        final ResponseEntity<DangerousGoodApi> response = controller.getDangerousGood(SHIPMENT_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(api, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenShipmentHasNoDangerousGood() {
        when(shipmentPort.loadDangerousGood(any(ShipmentId.class))).thenReturn(Optional.empty());

        final ResponseEntity<DangerousGoodApi> response = controller.getDangerousGood(SHIPMENT_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReplaceDangerousGoodWithPut() {
        final DangerousGood dangerousGood = dangerousGood();
        final DangerousGoodApi api = api("Replacement");
        when(requestMapper.map(api)).thenReturn(dangerousGood);
        when(responseMapper.map(dangerousGood)).thenReturn(api);

        final ResponseEntity<DangerousGoodApi> response = controller.putDangerousGood(SHIPMENT_ID, api);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(shipmentPort).putDangerousGood(any(ShipmentId.class), any(DangerousGood.class));
    }

    @Test
    void shouldMergeOnlyFieldsPresentInPatch() throws Exception {
        final DangerousGood current = dangerousGood();
        final DangerousGood updated = dangerousGood();
        final DangerousGoodApi currentApi = api("Original");
        final DangerousGoodApi updatedApi = api("Changed");
        when(shipmentPort.loadDangerousGood(any(ShipmentId.class))).thenReturn(Optional.of(current));
        when(responseMapper.map(current)).thenReturn(currentApi);
        when(requestMapper.map(any(DangerousGoodApi.class))).thenReturn(updated);
        when(responseMapper.map(updated)).thenReturn(updatedApi);

        final ResponseEntity<DangerousGoodApi> response = controller.patchDangerousGood(
                SHIPMENT_ID,
                new ObjectMapper().readTree("{\"description\":\"Changed\"}")
        );

        final ArgumentCaptor<DangerousGoodApi> requestCaptor = ArgumentCaptor.forClass(DangerousGoodApi.class);
        verify(requestMapper).map(requestCaptor.capture());
        assertEquals("UN3480", requestCaptor.getValue().unNumber());
        assertEquals("Changed", requestCaptor.getValue().description());
        assertEquals(updatedApi, response.getBody());
    }

    @Test
    void shouldNotPersistEmptyPatch() throws Exception {
        final DangerousGood current = dangerousGood();
        when(shipmentPort.loadDangerousGood(any(ShipmentId.class))).thenReturn(Optional.of(current));
        when(responseMapper.map(current)).thenReturn(api("Original"));

        controller.patchDangerousGood(SHIPMENT_ID, new ObjectMapper().readTree("{}"));

        verify(shipmentPort, never()).putDangerousGood(any(ShipmentId.class), any(DangerousGood.class));
    }

    @Test
    void shouldReportMissingDangerousGoodForPatch() throws Exception {
        when(shipmentPort.loadDangerousGood(any(ShipmentId.class))).thenReturn(Optional.empty());

        assertThrows(
                DangerousGoodNotFoundException.class,
                () -> controller.patchDangerousGood(SHIPMENT_ID, new ObjectMapper().readTree("{\"toxic\":true}"))
        );
    }

    @Test
    void shouldDeleteDangerousGoodIdempotently() {
        final ResponseEntity<Void> response = controller.deleteDangerousGood(SHIPMENT_ID);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(shipmentPort).deleteDangerousGood(any(ShipmentId.class));
    }

    private static DangerousGoodApi api(final String description) {
        return new DangerousGoodApi(
                "UN3480",
                "Lithium ion batteries",
                description,
                "9",
                null,
                null,
                "II",
                BigDecimal.ONE,
                "KILOGRAM",
                1,
                "BOX",
                false,
                false,
                false,
                false,
                "2",
                null,
                null,
                "112",
                null,
                "sds",
                null,
                "ADR",
                "ROAD",
                true,
                false,
                false,
                "flammable",
                "KEEP_DRY",
                "Handle with care",
                CountryCode.PL
        );
    }
}
