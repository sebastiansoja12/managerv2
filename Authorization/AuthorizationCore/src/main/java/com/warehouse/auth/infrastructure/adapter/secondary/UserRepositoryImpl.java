package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToModelMapper;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final UserReadRepository repository;

    public UserRepositoryImpl(final UserReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse createOrUpdate(final User user) {
        final UserEntity userEntity = UserToEntityMapper.map(user);

        repository.saveAndFlush(userEntity);

        return UserResponse.from(userEntity);
    }

    @Override
    public User findByUsername(final String username) {
        return repository.findByUsername(username).map(UserToModelMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    @Override
    public User findByApiKey(final String apiKey) {
        return repository.findByApiKey(apiKey).map(UserToModelMapper::map).orElse(null);
    }

    @Override
    public User findById(final UserId userId) {
        return repository.findById(userId).map(UserToModelMapper::map).orElse(null);
    }

    @Override
    public List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode) {
        return repository.findAll().stream().map(UserToModelMapper::map)
                .filter(user -> !user.isDeleted())
                .filter(user -> user.getDepartmentCode().equals(departmentCode))
                .map(User::getUserId)
                .collect(Collectors.toList());
    }
}
