package com.warehouse.auth;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
import com.warehouse.auth.configuration.AuthTestConfiguration;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = AuthTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/refresh_token.xml")
public class RefreshTokenReadRepositoryTest {

    @Autowired
    private RefreshTokenReadRepository repository;

    @Test
    void shouldFindByToken() {
        // given
        final String token = "12345";
        // when
        final Optional<RefreshTokenEntity> refreshToken = repository.findByToken(token);
        // then
        assertTrue(refreshToken.isPresent());
    }

    @Test
    void shouldNotFindByToken() {
        // given
        final String token = "0";
        // when
        final Optional<RefreshTokenEntity> refreshToken = repository.findByToken(token);
        // then
        assertTrue(refreshToken.isEmpty());
    }

    @Test
    void shouldDeleteByToken() {
        // given
        final String token = "12345";
        // when
        repository.deleteByToken(token);
        // then
        assertFalse(repository.findByToken(token).isPresent());
    }

    @Test
    void shouldNotDeleteByToken() {
        // given
        final String token = "12345";
        // when
        repository.deleteByToken("0");
        // then
        assertFalse(repository.findByToken(token).isEmpty());
    }
}
