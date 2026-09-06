package com.warehouse.auth;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.service.RefreshTokenGenerator;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.service.UserServiceImpl;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private RefreshTokenGenerator refreshTokenGenerator;

    @Mock
    private DepartmentServicePort departmentServicePort;

    private UserService userService;

    @BeforeEach
    void setup() {
        final ApplicationContext applicationContext = mock(ApplicationContext.class);
        final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        final DomainRegistry domainContext = new DomainRegistry();
        domainContext.setApplicationEventPublisher(eventPublisher);
        domainContext.setApplicationContext(applicationContext);
        userService = new UserServiceImpl(userRepository, departmentServicePort);
    }

    @Test
    void shouldCreate() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentId(10L), "");

        when(departmentServicePort.getDepartmentCode(new DepartmentId(10L)))
                .thenReturn(new DepartmentCode("TST"));
        // when
        final RegisterResponse registerResponse = userService.create(user);
        // then
        assertThat(registerResponse.userResponse().username()).isNotNull();
    }

    private <T> T expectedToBe(T value) {
        return value;
    }
}
