package com.warehouse.auth;

import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.AuthenticationService;
import com.warehouse.auth.domain.service.AuthenticationServiceImpl;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.authority.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        authenticationService = new AuthenticationServiceImpl(userRepository);
    }

    @Test
    void shouldRegister() {
        // given
        final User user = User.builder()
                .username("s-soja")
                .depotCode("TST")
                .email("test@test.pl")
                .firstName("test")
                .lastName("test")
                .password("password")
                .role(Role.ADMIN.name())
                .build();

        doReturn(mock(UserResponse.class))
                .when(userRepository)
                .signup(user);
        // when
        final RegisterResponse registerResponse = authenticationService.register(user);
        // then
        assertThat(registerResponse.getUserRegisterResponse().getId()).isNotNull();
    }
}
