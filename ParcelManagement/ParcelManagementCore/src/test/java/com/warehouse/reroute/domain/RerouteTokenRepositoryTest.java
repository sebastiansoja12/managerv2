package com.warehouse.reroute.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.domain.vo.ParcelId;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenRepositoryImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RerouteTokenMapper;

public class RerouteTokenRepositoryTest {

    private RerouteTokenRepository rerouteTokenRepository;

    @Mock
    private RerouteTokenReadRepository rerouteTokenReadRepository;

    @Mock
    private RerouteTokenMapper rerouteTokenMapper;

    private final Integer TOKEN = 12345;
    private final Long PARCEL_ID = 67890L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rerouteTokenRepository = new RerouteTokenRepositoryImpl(rerouteTokenMapper, rerouteTokenReadRepository);
    }

    @Test
    public void shouldReturnRerouteTokenByParcelIdAndTokenValue() {
        // given
        final RerouteToken rerouteToken = new RerouteToken();
        final RerouteTokenEntity rerouteTokenEntity = new RerouteTokenEntity();

        when(rerouteTokenReadRepository.loadByTokenAndParcelId(TOKEN, PARCEL_ID))
                .thenReturn(Optional.of(rerouteTokenEntity));
        when(rerouteTokenMapper.map(rerouteTokenEntity)).thenReturn(rerouteToken);

        // when
        final RerouteToken result = rerouteTokenRepository.loadByTokenAndParcelId(TOKEN, PARCEL_ID);

        // then
        assertEquals(rerouteToken, result);
    }

    @Test
    public void shouldThrowExceptionWhenRerouteTokenWasNotFound() {
        // given
        final Token token = Token.builder()
                .value(123).build();
        final ParcelId parcelId = new ParcelId(PARCEL_ID);
        when(rerouteTokenReadRepository.loadByTokenAndParcelId(token.getValue(), PARCEL_ID))
                .thenReturn(Optional.empty());

        // when && then
        assertThrows(RerouteTokenNotFoundException.class,
                () -> rerouteTokenRepository.loadByTokenAndParcelId(TOKEN, PARCEL_ID));
    }

    @Test
    public void shouldReturnRerouteTokenByTokenValue() {
        // given
        final Token token = Token.builder()
                .value(TOKEN).build();
        final RerouteToken rerouteToken = new RerouteToken();
        final RerouteTokenEntity rerouteTokenEntity = new RerouteTokenEntity();

        when(rerouteTokenReadRepository.findByToken(TOKEN)).thenReturn(Optional.of(rerouteTokenEntity));
        when(rerouteTokenMapper.map(rerouteTokenEntity)).thenReturn(rerouteToken);

        // when
        final RerouteToken result = rerouteTokenRepository.findByToken(token);

        // then
        assertEquals(rerouteToken, result);
    }
}