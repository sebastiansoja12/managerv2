package com.warehouse.reroute.infrastructure.adapter.primary.mapper;


import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.model.RerouteResponse;
import com.warehouse.reroute.domain.vo.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.api.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PrimaryResponseMapperTest {

    private PrimaryResponseMapper mapper;

    private final static Integer TOKEN = 123456;

    private final static java.lang.Long PARCEL_ID = 12345L;

    @BeforeEach
    void setUp() {
       mapper = new PrimaryResponseMapperImpl();
    }

    @Test
    void shouldMapFromRerouteResponseDtoToRerouteResponse() {
        // given
        final RerouteResponseDto rerouteResponseDto = new RerouteResponseDto(
                TOKEN, PARCEL_ID
        );
        // when
        final RerouteResponse rerouteResponse = mapper.map(rerouteResponseDto);

        // then
        assertThat(rerouteResponse.getParcelId()).isEqualTo(PARCEL_ID);
        assertThat(rerouteResponse.getToken()).isEqualTo(rerouteResponseDto.getToken());
    }

    @Test
    void shouldMapFromParcelResponseToParcelResponseDto() {
        // given
        final ParcelUpdateResponse parcelUpdateResponse = ParcelUpdateResponse.builder()
                .parcelId(new ParcelId(PARCEL_ID))
                .parcelType(ParcelType.AVERAGE)
                .recipient(Recipient.builder().build())
                .sender(Sender.builder().build())
                .build();
        // when
        final ParcelResponseDto parcelResponseDto = mapper.map(parcelUpdateResponse);

        // then
        assertThat(parcelResponseDto.getParcelId().getValue()).isEqualTo(PARCEL_ID);
    }

    @Test
    void shouldMapFromRerouteTokenResponseDtoToRerouteTokenResponse() {
        // given
        final RerouteTokenResponseDto rerouteTokenResponseDto = new RerouteTokenResponseDto();
        rerouteTokenResponseDto.setToken(TOKEN);
        rerouteTokenResponseDto.setParcelId(PARCEL_ID);
        rerouteTokenResponseDto.setValid(true);
        rerouteTokenResponseDto.setCreated("2022-08-10 21:37:00");

        // when
        final RerouteTokenResponse tokenResponse = mapper.map(rerouteTokenResponseDto);
        // then
        assertThat(tokenResponse.getToken()).isEqualTo(TOKEN);
        assertThat(tokenResponse.isValid()).isTrue();
    }
}
