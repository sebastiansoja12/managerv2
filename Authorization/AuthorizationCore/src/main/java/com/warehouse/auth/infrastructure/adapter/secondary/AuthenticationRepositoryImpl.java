package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.RefreshTokenEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AuthenticationRepositoryImpl implements UserRepository {

    private final AuthenticationReadRepository repository;

    private final RefreshTokenReadRepository refreshTokenReadRepository;

    private final UserMapper userMapper;


    @Override
    public AuthenticationResponse login(AuthenticationResponse authentication) {
        final RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(authentication.getRefreshToken());
        //refreshToken.setCreatedDate(authentication.getCreatedAt());
        refreshTokenReadRepository.save(refreshToken);
        return authentication;
    }

    @Override
    public UserResponse signup(User user) {
        final UserEntity userEntity = userMapper.map(user);

        repository.save(userEntity);

        return UserResponse.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .depotCode(userEntity.getDepotCode())
                .nonExpired(userEntity.isAccountNonExpired())
                .enabled(userEntity.isEnabled())
                .nonLocked(userEntity.isAccountNonLocked())
                .build();
    }

    @Override
    public void logout(String token) {
        refreshTokenReadRepository.deleteByToken(token);
    }
}
