package com.warehouse.reroute.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.domain.vo.RerouteTokenResponse;

@ExtendWith(MockitoExtension.class)
public class RerouteServiceImplTest {


    @Mock
    private RerouteService service;

    private final InMemoryRerouteTokenDatabase database = new InMemoryRerouteTokenDatabase();

    @Test
    void shouldFindByToken() {
        // given
        final Token token = Token.builder()
                .value(12345)
                .build();

        final RerouteToken rerouteToken = RerouteToken.builder()
                .token(token.getValue())
                .id(1L)
                .createdDate(Instant.now())
                .expiryDate(Instant.now().plusSeconds(100L))
                .parcelId(100001L)
                .build();

        final RerouteTokenResponse expectedResponse = RerouteTokenResponse.builder()
                .parcelId(new ParcelId(100001L))
                .token(token.getValue())
                .valid(true)
                .build();

        // insert into fake liquibase
        database.insertRerouteToken(rerouteToken);

        // when
        final RerouteToken rerouteTokenResponse = database.findRerouteTokenByTokenValue(token.getValue());
        // then
        assertThat(rerouteTokenResponse.getToken()).isEqualTo(12345);
    }


    @Test
    void shouldThrowRerouteTokenNotFoundException() {

    }

    @Test
    void shouldThrowConnectionErrorException() {

    }


}
