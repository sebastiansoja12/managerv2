package com.warehouse.auth;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    
    @Mock
    private AuthenticationReadRepository authenticationReadRepository;
    
    @Mock
    private RefreshTokenReadRepository refreshTokenReadRepository;

    private UserRepository userRepository;
    
    @BeforeEach
    void setup() {
		userRepository = new AuthenticationRepositoryImpl(authenticationReadRepository);
    }

    @Test
    void shouldCreateOrUpdate() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", User.Role.USER,
                new DepartmentCode("TST"), "");
        // when
        userRepository.createOrUpdate(user);
        // then
        verify(authenticationReadRepository).save(any(UserEntity.class));
    }
}
