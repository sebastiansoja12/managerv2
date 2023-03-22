package com.warehouse.reroute.domain.port.primary;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.vo.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RerouteServicePortImplTest {

    @Mock
    private RerouteServicePort port;

    private final Integer TOKEN_VALUE = 27150;

    private final Long PARCEL_ID = 123456L;

    @Test
    void shouldFindByToken() {
        // given
        final Token token = Token.builder()
                .value(TOKEN_VALUE)
                .build();
        when(port.findByToken(token)).thenReturn(
                RerouteTokenResponse.builder()
                        .valid(true)
                        .token(TOKEN_VALUE)
                        .parcelId(new ParcelId(PARCEL_ID))
                        .build());
        // when
        final RerouteTokenResponse response = port.findByToken(token);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(TOKEN_VALUE);
    }

    @Test
    void shouldLoadByTokenAndParcelId() {
        // given
        final Token token = Token.builder()
                .value(TOKEN_VALUE)
                .build();
        final ParcelId parcelId = new ParcelId(PARCEL_ID);
        final RerouteTokenResponse rerouteTokenResponse = RerouteTokenResponse.builder()
                .valid(true)
                .token(TOKEN_VALUE)
                .parcelId(new ParcelId(PARCEL_ID))
                .build();
        when(port.loadByTokenAndParcelId(token, parcelId)).thenReturn(rerouteTokenResponse);
        // when
        final RerouteTokenResponse actual = port.loadByTokenAndParcelId(token, parcelId);
        // then
        assertAll(
                () -> assertThat(actual.getToken().intValue()).isEqualTo(TOKEN_VALUE),
                () -> assertThat(actual.getParcelId().getValue()).isEqualTo(PARCEL_ID)
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

        when(port.sendReroutingInformation(rerouteRequest)).thenReturn(expectedResponse);
        // when
        final RerouteResponse actualResponse = port.sendReroutingInformation(rerouteRequest);
        // then
        assertAll(
                () -> assertThat(actualResponse.getToken().intValue()).isEqualTo(TOKEN_VALUE),
                () -> assertThat(actualResponse.getParcelId()).isEqualTo(PARCEL_ID)
        );
    }

    @Test
    void shouldUpdate() {
        // given
        final UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcel(Parcel.builder()
                        .parcelType(ParcelType.TEST)
                        .recipient(Recipient.builder().build())
                        .sender(Sender.builder().build())
                        .build()
                )
                .token(TOKEN_VALUE)
                .id(PARCEL_ID)
                .build();
        final ParcelUpdateResponse expectedResponse = ParcelUpdateResponse.builder()
                .parcelId(new ParcelId(PARCEL_ID))
                .parcelType(ParcelType.TEST)
                .recipient(Recipient.builder().build())
                .sender(Sender.builder().build())
                .build();
        when(port.update(updateParcelRequest)).thenReturn(expectedResponse);
        // when
        final ParcelUpdateResponse actualResponse = port.update(updateParcelRequest);

        // then
        assertAll(
                () -> assertThat(actualResponse.getSender().getFirstName()).isEqualTo(null),
                () -> assertThat(actualResponse.getParcelId().getValue()).isEqualTo(PARCEL_ID)
        );
    }
}
