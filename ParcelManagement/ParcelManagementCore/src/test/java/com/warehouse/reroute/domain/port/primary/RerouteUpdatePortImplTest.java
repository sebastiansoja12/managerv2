package com.warehouse.reroute.domain.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;

import com.warehouse.reroute.domain.vo.ParcelId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcel;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.domain.vo.Sender;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RerouteUpdatePortImplTest {

    @Mock
    private RerouteService rerouteService;

    @Mock
    private ParcelReroutePort parcelReroutePort;

    private RerouteUpdatePortImpl port;

    private final Integer TOKEN_VALUE = 27150;

    private final Long PARCEL_ID = 123456L;

    @BeforeEach
    void setup() {
        port = new RerouteUpdatePortImpl(rerouteService, parcelReroutePort);
    }

    @Test
    void shouldUpdate() {
        // given
        final RerouteParcelRequest updateParcelRequest = RerouteParcelRequest.builder()
                .parcel(RerouteParcel.builder()
                        .parcelSize(Size.TEST)
                        .recipient(Recipient.builder().build())
                        .sender(Sender.builder().build())
                        .status(Status.CREATED)
                        .parcelType(ParcelType.PARENT)
                        .build())
                .token(TOKEN_VALUE)
                .id(PARCEL_ID)
                .build();

        final RerouteToken rerouteToken = new RerouteToken(1L, 12345, Instant.now(),
                Instant.now().plusSeconds(600L), PARCEL_ID);

        when(rerouteService.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteToken);
        when(parcelReroutePort.reroute(updateParcelRequest.getParcel(), new ParcelId(PARCEL_ID))).thenReturn(mock(Parcel.class));
        // when
        final RerouteParcelResponse actualResponse = port.update(updateParcelRequest);

        // then
        assertThat(actualResponse).isNotNull();
    }

    @Test
    void shouldThrowRerouteTokenNotFoundException() {
        // given
        final RerouteParcelRequest updateParcelRequest = RerouteParcelRequest.builder()
                .parcel(RerouteParcel.builder()
                        .parcelSize(Size.TEST)
                        .recipient(Recipient.builder().build())
                        .sender(Sender.builder().build())
                        .status(Status.CREATED)
                        .parcelType(ParcelType.PARENT)
                        .build())
                .token(TOKEN_VALUE)
                .id(PARCEL_ID)
                .build();

        final RerouteToken rerouteToken = new RerouteToken(1L, 12345, Instant.now(),
                Instant.now(), PARCEL_ID);

        when(rerouteService.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteToken);
        // when
        final Executable executable = () -> port.update(updateParcelRequest);

        // then
        final RerouteTokenNotFoundException exception =
                assertThrows(RerouteTokenNotFoundException.class, executable);
        assertEquals("Reroute token is not valid", exception.getMessage());
    }
}
