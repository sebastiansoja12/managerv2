package com.warehouse.reroute;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.reroute.configuration.RerouteTokenTestConfiguration;
import com.warehouse.reroute.domain.model.Token;
import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RerouteTokenTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RerouteTokenReadRepositoryTest {

    @Autowired
    private RerouteTokenReadRepository repository;

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldReturnRerouteToken() {
        // given
        final Token token = Token.builder()
                .value(12345)
                .build();
        // when
        final Optional<RerouteTokenEntity> rerouteToken = repository.findByToken(token.getValue());
        // then
        assertThat(rerouteToken).isNotNull();
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldNotReturnRerouteToken() {
        // given
        final Token token = Token.builder()
                .value(0)
                .build();
        // when
        final Optional<RerouteTokenEntity> rerouteToken = repository.findByToken(token.getValue());
        // then
        assertThat(rerouteToken).isEmpty();
    }

}
