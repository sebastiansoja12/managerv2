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
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private final OperatorFilteredRepository<UserEntity> repository;

    public UserRepositoryImpl(final OperatorFilteredRepository<UserEntity> repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse createOrUpdate(final User user) {
        final UserEntity userEntity = UserToEntityMapper.map(user);

        if (findById(user.getUserId()) == null) {
            repository.create(userEntity);
        } else {
            repository.update(userEntity);
        }

        return UserResponse.from(userEntity);
    }

    @Override
    public User findByUsername(final String username) {
        return repository.createCriteria(UserEntity.class)
                .eq("username", username)
                .one()
                .map(UserToModelMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    @Override
    public User findByApiKey(final String apiKey) {
        return repository.createCriteria(UserEntity.class)
                .eq("apiKey", apiKey)
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    public User findById(final UserId userId) {
        return repository.createCriteria(UserEntity.class)
                .eq("userId.value", userId.value())
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.createCriteria(UserEntity.class)
                .isFalse("deleted")
                .list()
                .stream()
                .map(UserToModelMapper::map)
                .toList();
    }

    @Override
    public List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode) {
        return repository.createCriteria(UserEntity.class)
                .eq("departmentCode.value", departmentCode)
                .isFalse("deleted")
                .list()
                .stream()
                .map(UserToModelMapper::map)
                .map(User::getUserId)
                .toList();
    }

    @Override
    public User findByEmail(final String email) {
        return repository.createCriteria(UserEntity.class)
                .eq("email", email)
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserId findInitialUser() {
        return repository.createCriteria(UserEntity.class)
                .isTrue("initial")
                .one()
                .map(UserEntity::getUserId)
                .orElse(null);
    }
}
