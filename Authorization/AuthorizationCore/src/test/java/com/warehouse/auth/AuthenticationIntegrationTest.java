package com.warehouse.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.auth.configuration.AuthTestConfiguration;
import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = AuthTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/user_repository.xml")
public class AuthenticationIntegrationTest {

    @Autowired
    private AuthenticationPort authenticationPort;

    @Autowired
    private AuthenticationReadRepository repository;

    private final static String USERNAME = "s-soja";

    private final static String PASSWORD = "password";

    @Test
    void shouldLoginUser() {
        // given
        final LoginRequest loginRequest = new LoginRequest(USERNAME, PASSWORD);
        // when
        final AuthenticationResponse response = authenticationPort.login(loginRequest);
        // then
        assertThat(response.getAuthenticationToken()).isEqualTo("XD");
    }

    @Test
    void shouldNotLoginUserWhenBadCredentialsAreProvided() {
        // given
        final LoginRequest loginRequest = new LoginRequest("fakeUsername", PASSWORD);
        // when
        final Executable executable = () -> authenticationPort.login(loginRequest);
        // then
        final BadCredentialsException exception = assertThrows(BadCredentialsException.class, executable);
        assertEquals(expectedToBe("Bad credentials"), exception.getMessage());
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.ADMIN)
                .email("sebastian5152@wp.pl")
                .depotCode("TST")
                .firstName("Sebastian")
                .lastName("Soja")
                .build();
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
