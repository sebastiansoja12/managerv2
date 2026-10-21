package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.vo.ApiKey;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToModelMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final OperatorFilteredRepository<UserEntity> repository;

    private final NoOperatorContextRepository<UserEntity> noOperatorContextRepository;

    private final ApiKeyEncoder apiKeyEncoder;

    public UserRepositoryImpl(final OperatorFilteredRepository<UserEntity> repository,
                              final NoOperatorContextRepository<UserEntity> noOperatorContextRepository,
                              final ApiKeyEncoder apiKeyEncoder) {
        this.repository = repository;
        this.noOperatorContextRepository = noOperatorContextRepository;
        this.apiKeyEncoder = apiKeyEncoder;
    }

    @Override
    public void createOrUpdate(final OrganizationUser user) {
        final UserEntity userEntity = UserToEntityMapper.map(user);

        if (findById(user.getUserId()) == null) {
            repository.create(userEntity);
        } else {
            repository.update(userEntity);
        }

    }

    @Override
    public OrganizationUser findByUsername(final String username) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("username", username)
                .one()
                .map(UserToModelMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    @Override
    public OrganizationUser findByApiKey(final String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }

        final OrganizationUser userWithDisplayedApiKey = findByStoredApiKey(apiKey);
        if (userWithDisplayedApiKey != null) {
            return userWithDisplayedApiKey;
        }

        final String encodedApiKey = apiKeyEncoder.decode(new ApiKey(null, apiKey)).key();
        return findByStoredApiKey(encodedApiKey);
    }

    private OrganizationUser findByStoredApiKey(final String apiKey) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("apiKey", apiKey)
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    public OrganizationUser findById(final UserId userId) {
        return repository.createCriteria(UserEntity.class)
                .eq("userId.value", userId.value())
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    public List<OrganizationUser> findAll() {
        return repository.createCriteria(UserEntity.class)
                .isFalse("deleted")
                .list()
                .stream()
                .map(UserToModelMapper::map)
                .toList();
    }

    @Override
    public List<UserId> findAllActiveUsersByDepartmentId(final DepartmentId departmentId) {
        return repository.createCriteria(UserEntity.class)
                .eq("departmentId.value", departmentId.value())
                .isFalse("deleted")
                .list()
                .stream()
                .map(UserToModelMapper::map)
                .map(OrganizationUser::getUserId)
                .toList();
    }

    @Override
    public OrganizationUser findByEmail(final String email) {
        return repository.createCriteria(UserEntity.class)
                .eq("email", email)
                .one()
                .map(UserToModelMapper::map)
                .orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UserId findInitialUser() {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .isTrue("initial")
                .one()
                .map(UserEntity::getUserId)
                .orElse(null);
    }

    @Override
    public Optional<OrganizationUser> findActiveSuperadmin(final OperatorId operatorId) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("operatorId.value", operatorId.value())
                .eq("role", UserEntity.Role.SUPERADMIN)
                .isFalse("deleted")
                .desc("initial")
                .asc("createdAt")
                .list()
                .stream()
                .findFirst()
                .map(UserToModelMapper::map);
    }

    @Override
    public List<UserId> findActiveSuperadminIds(final OperatorId operatorId) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("operatorId.value", operatorId.value())
                .eq("role", UserEntity.Role.SUPERADMIN)
                .isFalse("deleted")
                .list()
                .stream()
                .map(UserEntity::getUserId)
                .toList();
    }
}
