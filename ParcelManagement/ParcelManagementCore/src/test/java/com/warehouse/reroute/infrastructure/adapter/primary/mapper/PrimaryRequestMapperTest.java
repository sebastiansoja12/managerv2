package com.warehouse.reroute.infrastructure.adapter.primary.mapper;

import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.model.Parcel;
import com.warehouse.reroute.domain.model.RerouteRequest;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.model.UpdateParcelRequest;
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
public class PrimaryRequestMapperTest {

    @Mock
    private PrimaryRequestMapper mapper;

    private final static long PARCEL_ID = 100001L;

    private final static String EMAIL = "sebastian5152@wp.pl";

    private final static Integer TOKEN = 123456;

    @Test
    void shouldMapFromRerouteRequestDtoToRerouteRequest() {
        // given
        final RerouteRequestDto requestDto = new RerouteRequestDto();
        requestDto.setParcelId(parcelId());
        requestDto.setEmail(email());
        when(mapper.map(requestDto)).thenReturn(RerouteRequest.builder()
                .parcelId(PARCEL_ID)
                .email(EMAIL)
                .build());
        // when
        final RerouteRequest request = mapper.map(requestDto);
        // then
        assertThat(request).isNotNull();
        assertThat(request.getParcelId()).isEqualTo(PARCEL_ID);
        assertThat(request.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void shouldMapFromUpdateParcelRequestDtoToUpdateParcelRequest() {
        // given
        final UpdateParcelRequestDto requestDto = new UpdateParcelRequestDto();
        requestDto.setParcelId(parcelIdDto());
        requestDto.setToken(tokenDto());
        requestDto.setParcel(parcelDto());
        when(mapper.map(requestDto)).thenReturn(UpdateParcelRequest.builder()
                .id(PARCEL_ID)
                .parcel(parcel())
                .token(TOKEN)
                .build());
        // when
        final UpdateParcelRequest updateParcelRequest = mapper.map(requestDto);
        // then
        assertThat(updateParcelRequest).isNotNull();
        assertThat(updateParcelRequest.getId()).isEqualTo(PARCEL_ID);
        assertThat(updateParcelRequest.getToken()).isEqualTo(TOKEN);
    }

    @Test
    void shouldMapFromParcelDtoToParcel() {
        // given
        final ParcelDto parcelDto = parcelDto();
        when(mapper.map(parcelDto)).thenReturn(Parcel.builder()
                .parcelSize(Size.AVERAGE)
                .recipient(Recipient.builder().build())
                .sender(Sender.builder().build())
                .build());

        // when
        final Parcel parcel = mapper.map(parcelDto);
        // then
        assertThat(parcel.getParcelSize().getSize()).isEqualTo(parcelDto.getParcelSize().getSize());
    }

    @Test
    void shouldMapFromTokenDtoToToken() {
        // given
        final TokenDto tokenDto = tokenDto();
        // when
        when(mapper.map(tokenDto)).thenReturn(Token.builder().value(TOKEN).build());
        final Token token = mapper.map(tokenDto);
        // then
        assertThat(token.getValue()).isEqualTo(TOKEN);

    }

    @Test
    void shouldMapFromParcelIdDtoToParcelId() {
        // given
        final ParcelIdDto parcelIdDto = parcelIdDto();
        when(mapper.map(parcelIdDto)).thenReturn(new com.warehouse.reroute.domain.vo.ParcelId(PARCEL_ID));
        // when
        final com.warehouse.reroute.domain.vo.ParcelId aParcelId = mapper.map(parcelIdDto);
        // then
        assertThat(aParcelId.getValue()).isEqualTo(PARCEL_ID);
    }

    private com.warehouse.reroute.infrastructure.api.dto.ParcelId parcelId() {
        final com.warehouse.reroute.infrastructure.api.dto.ParcelId parcelId = new com.warehouse.reroute.infrastructure.api.dto.ParcelId();
        parcelId.setValue(PARCEL_ID);
        return parcelId;
    }

    private EmailDto email() {
        final EmailDto email = new EmailDto();
        email.setValue(EMAIL);
        return email;
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

    private Parcel parcel() {
        return Parcel.builder().build();
    }
}
