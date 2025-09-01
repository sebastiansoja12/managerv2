package com.warehouse.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.AuthenticationRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapperImpl;
import com.warehouse.commonassets.identificator.DepartmentCode;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    
    @Mock
    private AuthenticationReadRepository authenticationReadRepository;
    
    @Mock
    private RefreshTokenReadRepository refreshTokenReadRepository;
    
    private final UserMapper userMapper = new UserMapperImpl();
    
    private UserRepository userRepository;
    
    @BeforeEach
    void setup() {
		userRepository = new AuthenticationRepositoryImpl(authenticationReadRepository, userMapper);
    }

    @Test
    void shouldSaveUser() {
        // given
        final User user = new User(null, "s-soja", "test", "Sebastian", "Soja", "sebastian5152@wp.pl", Role.USER,
                new DepartmentCode("TST"), "");
        // when
        userRepository.saveUser(user);
        // then
        verify(authenticationReadRepository).save(any(UserEntity.class));
    }
}
