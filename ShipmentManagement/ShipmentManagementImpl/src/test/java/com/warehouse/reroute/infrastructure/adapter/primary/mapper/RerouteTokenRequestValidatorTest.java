package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.reroute.infrastructure.adapter.primary.api.*;
import org.junit.jupiter.api.Test;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.Token;


public class RerouteTokenRequestValidatorTest {


    private final RerouteTokenRequestMapper requestMapper = getMapper(RerouteTokenRequestMapper.class);

    private final static long PARCEL_ID = 100001L;

    private final static String EMAIL = "sebastian5152@wp.pl";

    private final static Integer TOKEN = 123456;

    @Test
    void shouldMapFromRerouteRequestDtoToRerouteRequest() {
        // given
        final RerouteRequestDto requestDto = RerouteRequestDto.builder()
                .parcelId(parcelId())
                .email(email())
                .build();
        // when
        final RerouteRequest request = requestMapper.map(requestDto);
        // then
        assertThat(request).isNotNull();
        assertThat(request.getParcelId()).isEqualTo(PARCEL_ID);
        assertThat(request.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void shouldMapFromUpdateParcelRequestDtoToUpdateParcelRequest() {
        // given
        final RerouteParcelRequestDto requestDto = RerouteParcelRequestDto.builder()
                .parcelId(parcelIdDto())
                .token(TokenDto.builder().value(TOKEN).build())
                .parcel(parcelDto())
                .build();
        // when
        final RerouteParcelRequest updateParcelRequest = requestMapper.map(requestDto);
        // then
        assertThat(updateParcelRequest).isNotNull();
        assertThat(updateParcelRequest.getId()).isEqualTo(PARCEL_ID);
        assertThat(updateParcelRequest.getToken()).isEqualTo(TOKEN);
    }

    @Test
    void shouldMapFromParcelDtoToParcel() {
        // given
        final ParcelDto parcelDto = parcelDto();
        // when
        final Parcel parcel = requestMapper.map(parcelDto);
        // then
        assertThat(parcel.getParcelSize().getSize()).isEqualTo(parcelDto.getParcelSize().getSize());
    }

    @Test
    void shouldMapFromTokenDtoToToken() {
        // given
        final TokenDto tokenDto = TokenDto.builder().value(TOKEN).build();
        // when
        final Token token = requestMapper.map(tokenDto);
        // then
        assertThat(token.getValue()).isEqualTo(TOKEN);

    }

    @Test
    void shouldMapFromParcelIdDtoToParcelId() {
        // given
        final ParcelIdDto parcelIdDto = parcelIdDto();
        // when
        final com.warehouse.reroute.domain.vo.ParcelId parcelId = requestMapper.map(parcelIdDto);
        // then
        assertThat(parcelId.getValue()).isEqualTo(PARCEL_ID);
    }

    private ParcelId parcelId() {
        return ParcelId.builder().value(PARCEL_ID).build();
    }

    private EmailDto email() {
        return EmailDto.builder().value(EMAIL).build();
    }

    private ParcelIdDto parcelIdDto() {
        return ParcelIdDto.builder().value(PARCEL_ID).build();
    }

    private ParcelDto parcelDto() {
        return ParcelDto.builder().parcelSize(ParcelSizeDto.AVERAGE)
                .recipient(RecipientDto.builder().build())
                .sender(SenderDto.builder().build())
                .build();
    }

}
