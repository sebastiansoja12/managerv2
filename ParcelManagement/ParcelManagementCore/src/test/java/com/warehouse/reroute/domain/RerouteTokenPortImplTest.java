package com.warehouse.reroute.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.exception.RerouteTokenExpiredException;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPortImpl;
import com.warehouse.reroute.domain.port.secondary.Logger;
import com.warehouse.reroute.domain.port.secondary.MailServicePort;
import com.warehouse.reroute.domain.port.secondary.ParcelReroutePort;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteServiceImpl;
import com.warehouse.reroute.domain.service.RerouteTokenGeneratorService;
import com.warehouse.reroute.domain.service.RerouteTokenGeneratorServiceImpl;
import com.warehouse.reroute.domain.vo.*;

@ExtendWith(MockitoExtension.class)
public class RerouteTokenPortImplTest {


    @Mock
    private MailServicePort mailServicePort;

    @Mock
    private RerouteTokenRepository rerouteTokenRepository;

    @Mock
    private ParcelReroutePort parcelReroutePort;

    @Mock
    private Logger logger;

    private RerouteTokenPortImpl port;

    private final Integer TOKEN_VALUE = 27150;

    private final Long PARCEL_ID = 123456L;

    @BeforeEach
    void setup() {
        final RerouteTokenGeneratorService rerouteTokenGeneratorService = new RerouteTokenGeneratorServiceImpl();
        final RerouteService rerouteService = new RerouteServiceImpl(mailServicePort, rerouteTokenRepository);
        port = new RerouteTokenPortImpl(rerouteService, parcelReroutePort, rerouteTokenGeneratorService, logger);
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
                Instant.now().plusSeconds(600L), PARCEL_ID, "");

        when(rerouteTokenRepository.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteToken);
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
                Instant.now(), PARCEL_ID, "");

        when(rerouteTokenRepository.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteToken);
        // when
        final Executable executable = () -> port.update(updateParcelRequest);

        // then
        final RerouteTokenExpiredException exception =
                assertThrows(RerouteTokenExpiredException.class, executable);
        assertEquals("Reroute token expired", exception.getMessage());
    }

    @Test
    void shouldFindByToken() {
        // given
        final Token token = new Token(TOKEN_VALUE);
        when(rerouteTokenRepository.findByToken(token)).thenReturn(
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
        when(rerouteTokenRepository.loadByTokenAndParcelId(TOKEN_VALUE, PARCEL_ID)).thenReturn(rerouteTokenResponse);
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
        final String email = "test.pl";
        final Integer token = 5309900;
        // create reroute request
        final RerouteRequest rerouteRequest = new RerouteRequest();
        rerouteRequest.setEmail(email);
        rerouteRequest.setParcelId(PARCEL_ID);

        final RerouteProcessor rerouteProcessor = new RerouteProcessor(
                email, PARCEL_ID, new GeneratedToken(token)
        );

        final RerouteToken savedRerouteToken = new RerouteToken(
                1L, token, Instant.now(), Instant.now(), PARCEL_ID, email
        );

        when(rerouteTokenRepository.saveReroutingToken(rerouteProcessor)).thenReturn(savedRerouteToken);
        // when
        final RerouteResponse actualResponse = port.sendReroutingInformation(rerouteRequest);
        // then
        assertAll(
                () -> assertThat(actualResponse.getToken().intValue()).isEqualTo(token),
                () -> assertThat(actualResponse.getParcelId()).isEqualTo(PARCEL_ID)
        );
    }
}
