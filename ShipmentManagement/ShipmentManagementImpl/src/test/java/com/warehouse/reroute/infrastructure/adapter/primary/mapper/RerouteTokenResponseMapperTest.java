package com.warehouse.reroute.infrastructure.adapter.primary.mapper;


import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.model.Recipient;
import com.warehouse.reroute.domain.model.Sender;
import com.warehouse.reroute.domain.vo.RerouteResponse;
import com.warehouse.reroute.domain.vo.*;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.adapter.primary.api.ParcelResponseDto;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteResponseDto;
import com.warehouse.reroute.infrastructure.adapter.primary.api.RerouteTokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RerouteTokenResponseMapperTest {

    private RerouteTokenResponseMapper responseMapper;

    private final static Integer TOKEN = 123456;

    private final static java.lang.Long PARCEL_ID = 12345L;

	@BeforeEach
	void setUp() {
		responseMapper = new RerouteTokenResponseMapperImpl();
	}

    @Test
    void shouldMapFromRerouteResponseDtoToRerouteResponse() {
        // given
        final RerouteResponseDto rerouteResponseDto = RerouteResponseDto.builder()
                .token(TOKEN)
                .parcelId(PARCEL_ID)
                .build();
        // when
        final RerouteResponse rerouteResponse = responseMapper.map(rerouteResponseDto);

        // then
        assertThat(rerouteResponse.getParcelId()).isEqualTo(PARCEL_ID);
        assertThat(rerouteResponse.getToken()).isEqualTo(rerouteResponseDto.getToken());
    }

    @Test
    void shouldMapFromParcelResponseToParcelResponseDto() {
        // given
        final RerouteParcelResponse rerouteParcelResponse = RerouteParcelResponse.builder()
                .parcelId(new ParcelId(PARCEL_ID))
                .parcelSize(Size.AVERAGE)
                .recipient(Recipient.builder().build())
                .sender(Sender.builder().build())
                .build();
        // when
        final ParcelResponseDto parcelResponseDto = responseMapper.map(rerouteParcelResponse);

        // then
        assertThat(parcelResponseDto.getParcelId().getValue()).isEqualTo(PARCEL_ID);
    }

    @Test
    void shouldMapFromRerouteTokenResponseDtoToRerouteTokenResponse() {
        // given
        final RerouteTokenResponseDto rerouteTokenResponseDto = RerouteTokenResponseDto.builder()
                .token(TOKEN)
                .parcelId(PARCEL_ID)
                .created("2022-08-10 21:37:00")
                .isValid(true)
                .build();
        // when
        final RerouteTokenResponse tokenResponse = responseMapper.map(rerouteTokenResponseDto);
        // then
        assertThat(tokenResponse.getToken()).isEqualTo(TOKEN);
        assertThat(tokenResponse.isValid()).isTrue();
    }
}
