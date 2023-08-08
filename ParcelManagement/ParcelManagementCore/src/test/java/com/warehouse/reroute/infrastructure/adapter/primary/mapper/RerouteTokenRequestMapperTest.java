package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.model.*;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;
import com.warehouse.reroute.infrastructure.api.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RerouteTokenRequestMapperTest {

    @Mock
    private RerouteTokenRequestMapper requestMapper;

    private final static long PARCEL_ID = 100001L;

    private final static String EMAIL = "sebastian5152@wp.pl";

    private final static Integer TOKEN = 123456;

    @Test
    void shouldMapFromRerouteRequestDtoToRerouteRequest() {
        // given
        final RerouteRequestDto requestDto = new RerouteRequestDto();
        requestDto.setParcelId(parcelId());
        requestDto.setEmail(email());
        when(requestMapper.map(requestDto)).thenReturn(RerouteRequest.builder()
                .parcelId(PARCEL_ID)
                .email(EMAIL)
                .build());
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
        requestDto.setToken(tokenDto());
        requestDto.setParcel(parcelDto());
        when(requestMapper.map(requestDto)).thenReturn(RerouteParcelRequest.builder()
                .id(PARCEL_ID)
                .parcel(parcel())
                .token(TOKEN)
                .build());
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
        when(requestMapper.map(parcelDto)).thenReturn(Parcel.builder()
                .parcelSize(Size.AVERAGE)
                .recipient(Recipient.builder().build())
                .sender(Sender.builder().build())
                .build());

        // when
        final Parcel parcel = requestMapper.map(parcelDto);
        // then
        assertThat(parcel.getParcelSize().getSize()).isEqualTo(parcelDto.getParcelSize().getSize());
    }

    @Test
    void shouldMapFromTokenDtoToToken() {
        // given
        final TokenDto tokenDto = tokenDto();
        // when
        when(requestMapper.map(tokenDto)).thenReturn(Token.builder().value(TOKEN).build());
        final Token token = requestMapper.map(tokenDto);
        // then
        assertThat(token.getValue()).isEqualTo(TOKEN);

    }

    @Test
    void shouldMapFromParcelIdDtoToParcelId() {
        // given
        final ParcelIdDto parcelIdDto = parcelIdDto();
        when(requestMapper.map(parcelIdDto)).thenReturn(new com.warehouse.reroute.domain.vo.ParcelId(PARCEL_ID));
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

    private TokenDto tokenDto() {
        final TokenDto token = new TokenDto();
        token.setValue(TOKEN);
        return token;
    }

    private ParcelDto parcelDto() {
        final ParcelDto parcel = new ParcelDto();
        parcel.setParcelSize(ParcelSizeDto.AVERAGE);
        parcel.setRecipient(new RecipientDto());
        parcel.setSender(new SenderDto());
        return parcel;
    }

    private RerouteParcel parcel() {
        return RerouteParcel.builder().build();
    }
}
