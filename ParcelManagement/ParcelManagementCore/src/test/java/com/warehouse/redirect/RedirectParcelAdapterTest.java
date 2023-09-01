package com.warehouse.redirect;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectParcelAdapter;
import com.warehouse.shipment.infrastructure.api.ShipmentService;

@ExtendWith(MockitoExtension.class)
public class RedirectParcelAdapterTest {

    @Mock
    private ShipmentService service;

    private RedirectParcelAdapter redirectParcelAdapter;


    @BeforeEach
    void setup() {
        redirectParcelAdapter = new RedirectParcelAdapter(service);
    }

    @Test
    void shouldExist() {
        // given
        final Long parcelId = 1L;

        doReturn(true)
                .when(service)
                .exists(parcelId);
        // when
        final boolean exists = redirectParcelAdapter.exists(parcelId);
        // then
        assertTrue(exists);
    }

    @Test
    void shouldNotExist() {
        // given
        final Long parcelId = 1L;

        doReturn(false)
                .when(service)
                .exists(parcelId);
        // when
        final boolean exists = redirectParcelAdapter.exists(parcelId);
        // then
        assertFalse(exists);
    }
}
