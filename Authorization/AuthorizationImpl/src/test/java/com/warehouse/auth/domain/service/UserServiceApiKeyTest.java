package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceApiKeyTest {

    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, mock(DepartmentServicePort.class));
        final DomainRegistry domainRegistry = new DomainRegistry();
        domainRegistry.setApplicationContext(mock(ApplicationContext.class));
        domainRegistry.setApplicationEventPublisher(mock(ApplicationEventPublisher.class));
    }

    @Test
    void shouldChangeApiKeyOnUser() {
        final UserId userId = new UserId(10L);
        final User user = new User(userId, "s-soja", "password", "Sebastian", "Soja",
                "sebastian5152@wp.pl", User.Role.USER, new DepartmentId(10L), "old-api-key");
        when(userRepository.findById(userId)).thenReturn(user);

        userService.changeApiKey(userId, "new-api-key");

        assertThat(user.getApiKey()).isEqualTo("new-api-key");
        verify(userRepository).createOrUpdate(user);
    }
}
