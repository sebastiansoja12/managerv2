package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserToModelMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.commonassets.security.CredentialCipher;
import java.util.Objects;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final OperatorFilteredRepository<UserEntity> repository;
    private final CredentialCipher credentialCipher;

    private final NoOperatorContextRepository<UserEntity> noOperatorContextRepository;

    public UserRepositoryImpl(final OperatorFilteredRepository<UserEntity> repository,
                              final NoOperatorContextRepository<UserEntity> noOperatorContextRepository,
                              final CredentialCipher credentialCipher) {
        this.repository = repository;
        this.credentialCipher = credentialCipher;
        this.noOperatorContextRepository = noOperatorContextRepository;
    }

    @Override
    public void createOrUpdate(final OrganizationUser user) {
        final UserEntity userEntity = UserToEntityMapper.map(user);

        final Optional<UserEntity> stored = findEntityById(user.getUserId());
        if (stored.isEmpty()) {
            repository.create(userEntity);
        } else {
            final UserEntity previous = stored.orElseThrow();
            if (userEntity.getApiKey() != null && Objects.equals(previous.getApiKey(), userEntity.getApiKey())
                    && !Boolean.TRUE.equals(userEntity.getDeleted())) {
                userEntity.setEncryptedApiKey(previous.getEncryptedApiKey());
            }
            repository.update(userEntity);
        }

    }

    @Override
    public void saveApiKey(final OrganizationUser user, final String secret) {
        final UserEntity entity = UserToEntityMapper.map(user);
        entity.setEncryptedApiKey(this.credentialCipher.encrypt(secret));
        this.repository.update(entity);
    }

    @Override
    public Optional<String> findApiKeySecret(final UserId userId) {
        return findEntityById(userId).filter(entity -> entity.getApiKey() != null && !Boolean.TRUE.equals(entity.getDeleted()))
                .map(UserEntity::getEncryptedApiKey).map(this.credentialCipher::decrypt);
    }

    private Optional<UserEntity> findEntityById(final UserId userId) {
        return this.repository.createCriteria(UserEntity.class).eq("userId.value", userId.value()).one();
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
    public Optional<OrganizationUser> findByApiKeyHash(final String apiKeyHash) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("apiKey", apiKeyHash)
                .one()
                .map(UserToModelMapper::map);
    }

    @Override
    public Optional<OrganizationUser> findByIdInOperator(final UserId userId, final OperatorId operatorId) {
        return noOperatorContextRepository.createCriteria(UserEntity.class)
                .eq("userId.value", userId.value())
                .eq("operatorId.value", operatorId.value())
                .one()
                .map(UserToModelMapper::map);
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
