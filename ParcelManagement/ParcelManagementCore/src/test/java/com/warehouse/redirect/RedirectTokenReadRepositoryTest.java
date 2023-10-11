package com.warehouse.redirect;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
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
import com.warehouse.redirect.configuration.RedirectTokenTestConfiguration;
import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectTokenReadRepository;
import com.warehouse.redirect.domain.vo.Token;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RedirectTokenTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/redirect_token.xml")
public class RedirectTokenReadRepositoryTest {

    @Autowired
    private RedirectTokenReadRepository repository;

    @Test
    void shouldReturnRedirectToken() {
        // given
        final Token token = new Token("12345");
        // when
        final Optional<RedirectTokenEntity> redirectToken = repository.findByToken(token.getValue());
        // then
        assertThat(redirectToken).isNotNull();
    }

    @Test
    void shouldNotReturnRedirectToken() {
        // given
        final Token token = new Token("0");

        // when
        final Optional<RedirectTokenEntity> redirectToken = repository.findByToken(token.getValue());
        // then
        assertThat(redirectToken).isEmpty();
    }
}
