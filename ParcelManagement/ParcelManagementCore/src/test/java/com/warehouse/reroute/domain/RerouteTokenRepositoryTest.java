package com.warehouse.reroute.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.reroute.configuration.RerouteTokenTestConfiguration;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.exception.RerouteTokenNotFoundException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RerouteTokenTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/rerouteToken.xml")
public class RerouteTokenRepositoryTest {


    @Autowired
    private RerouteTokenReadRepository rerouteTokenReadRepository;

    @Autowired
    private RerouteTokenRepository rerouteTokenRepository;

    private final Integer TOKEN = 12345;
    private final Long PARCEL_ID = 100001L;

    @Test
    public void shouldReturnRerouteTokenByParcelIdAndTokenValue() {
        // given
        final RerouteTokenEntity rerouteTokenEntity = RerouteTokenEntity.builder()
                .parcelId(PARCEL_ID)
                .token(TOKEN)
                .build();
        // when
        final RerouteToken result = rerouteTokenRepository.loadByTokenAndParcelId(TOKEN, PARCEL_ID);

        // then
        assertEquals(rerouteTokenEntity.getToken(), result.getToken());
    }

    @Test
    public void shouldThrowExceptionWhenRerouteTokenWasNotFound() {
        // given && when && then
        assertThrows(RerouteTokenNotFoundException.class,
                () -> rerouteTokenRepository.loadByTokenAndParcelId(0, PARCEL_ID));
    }

    @Test
    public void shouldReturnRerouteTokenByTokenValue() {
        // given
        final Token token = new Token(TOKEN);
        final RerouteTokenEntity rerouteTokenEntity = RerouteTokenEntity.builder()
                .token(TOKEN)
                .build();
        // when
        final RerouteToken result = rerouteTokenRepository.findByToken(token);

        // then
        assertEquals(rerouteTokenEntity.getToken(), result.getToken());
    }
}