package com.warehouse.reroute;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.reroute.configuration.RerouteTokenTestConfiguration;
import com.warehouse.reroute.domain.service.RerouteTokenValidatorService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RerouteTokenTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RerouteTokenValidatorServiceTest {

    @Autowired
    private RerouteTokenValidatorService validatorService;

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldValidate() {
        // given
        final Integer token = 54321;
        // when
        final boolean isValid = validatorService.validate(token);
        // then
        assertTrue(isValid);
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldNotValidate() {
        // given
        final Integer token = 0;
        // when
        final boolean isValid = validatorService.validate(token);
        // then
        assertFalse(isValid);
    }

    @Test
    @DatabaseSetup("/dataset/rerouteToken.xml")
    void shouldNotValidateWhenTokenIsExpired() {
        // given
        final Integer token = 21370;
        // when
        final boolean isValid = validatorService.validate(token);
        // then
        assertFalse(isValid);
    }

}
