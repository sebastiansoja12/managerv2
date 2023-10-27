package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mapstruct.factory.Mappers.getMapper;

import org.junit.jupiter.api.Test;

import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteParcelRequest;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.infrastructure.api.dto.*;



public class RerouteTokenRequestMapperTest {


    private final RerouteTokenRequestMapper requestMapper = getMapper(RerouteTokenRequestMapper.class);

    private final static long PARCEL_ID = 100001L;

    private final static String EMAIL = "sebastian5152@wp.pl";

    private final static Integer TOKEN = 123456;

    @Test
    void shouldMapFromRerouteRequestDtoToRerouteRequest() {
        // given
        final RerouteRequestDto requestDto = new RerouteRequestDto();
        requestDto.setParcelId(parcelId());
        requestDto.setEmail(email());
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
        final RerouteParcelRequestDto requestDto = new RerouteParcelRequestDto();
        requestDto.setParcelId(parcelIdDto());
        requestDto.setToken(new TokenDto(TOKEN));
        requestDto.setParcel(parcelDto());
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
        final TokenDto tokenDto = new TokenDto(TOKEN);
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

    private com.warehouse.reroute.infrastructure.api.dto.ParcelId parcelId() {
        return new ParcelId(PARCEL_ID);
    }

    private EmailDto email() {
        return new EmailDto(EMAIL);
    }

    private ParcelIdDto parcelIdDto() {
        final ParcelIdDto parcelId = new ParcelIdDto();
        parcelId.setValue(PARCEL_ID);
        return parcelId;
    }

    private ParcelDto parcelDto() {
        final ParcelDto parcel = new ParcelDto();
        parcel.setParcelSize(ParcelSizeDto.AVERAGE);
        parcel.setRecipient(new RecipientDto());
        parcel.setSender(new SenderDto());
        return parcel;
    }

}
