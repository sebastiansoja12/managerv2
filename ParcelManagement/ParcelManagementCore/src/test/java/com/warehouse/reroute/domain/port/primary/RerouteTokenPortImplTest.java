package com.warehouse.reroute.domain.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.secondary.Logger;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.RerouteParcelResponse;
import com.warehouse.reroute.domain.vo.Sender;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
public class RerouteTokenPortImplTest {

    @Mock
    private RerouteService rerouteService;

    @Mock
    private ParcelReroutePort parcelReroutePort;

    @Mock
    private Logger logger;

    private RerouteTokenPortImpl port;

    private final Integer TOKEN_VALUE = 27150;

    private final Long PARCEL_ID = 123456L;

    @BeforeEach
    void setup() {
        port = new RerouteTokenPortImpl(rerouteService, parcelReroutePort, logger);
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

    @Test
    void shouldFindByToken() {
        // given
        final Token token = Token.builder()
                .value(TOKEN_VALUE)
                .build();
        when(rerouteService.findByToken(token)).thenReturn(
                RerouteToken.builder()
                        .token(TOKEN_VALUE)
                        .parcelId(PARCEL_ID)
                        .build());
        // when
        final RerouteToken response = port.findByToken(token);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(TOKEN_VALUE);
    }

    @Test
    void shouldLoadByTokenAndParcelId() {
        // given
        final RerouteToken rerouteTokenResponse = RerouteToken.builder()
                .token(TOKEN_VALUE)
                .parcelId(PARCEL_ID)
                .build();
        when(rerouteService.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteTokenResponse);
        // when
        final RerouteToken actual = port.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID);
        // then
        assertAll(
                () -> assertThat(actual.getToken().intValue()).isEqualTo(TOKEN_VALUE),
                () -> assertThat(actual.getParcelId()).isEqualTo(PARCEL_ID)
        );
    }

    @Test
    void shouldSendReroutingInformation() {
        // given
        final RerouteRequest rerouteRequest = new RerouteRequest();
        rerouteRequest.setEmail("test.pl");
        rerouteRequest.setParcelId(PARCEL_ID);

        final RerouteResponse expectedResponse = RerouteResponse.builder()
                .parcelId(PARCEL_ID)
                .token(TOKEN_VALUE)
                .build();

        when(rerouteService.sendReroutingInformation(rerouteRequest)).thenReturn(expectedResponse);
        // when
        final RerouteResponse actualResponse = port.sendReroutingInformation(rerouteRequest);
        // then
        assertAll(
                () -> assertThat(actualResponse.getToken().intValue()).isEqualTo(TOKEN_VALUE),
                () -> assertThat(actualResponse.getParcelId()).isEqualTo(PARCEL_ID)
        );
    }
}
