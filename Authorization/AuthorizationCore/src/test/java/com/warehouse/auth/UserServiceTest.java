package com.warehouse.auth;

import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.service.UserServiceImpl;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.Token;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenGenerator refreshTokenGenerator;

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserServiceImpl(userRepository, refreshTokenRepository,
                refreshTokenGenerator);
    }

    @Test
    void shouldCreate() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");

        final UserResponse userResponse = UserResponse.builder()
                .nonLocked(false)
                .enabled(true)
                .nonExpired(false)
                .username("s-soja")
                .departmentCode(new DepartmentCode("TST"))
                .build();

        doReturn(userResponse)
                .when(userRepository)
                .createOrUpdate(user);
        // when
        final RegisterResponse registerResponse = userService.create(user);
        // then
        assertThat(registerResponse.userResponse().username()).isNotNull();
    }

    @Test
    void shouldLogin() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");

        final RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .build();

        final Token token = new Token("90b20782-8882-4f46-a14e-e0fa761fb7c8");

        doReturn(token)
                .when(refreshTokenRepository)
                .save(refreshToken);
        // when
        final LoginResponse response = userService.login(user);
        // then
        assertEquals(expectedToBe("90b20782-8882-4f46-a14e-e0fa761fb7c8"), response.getRefreshToken().getValue());
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
