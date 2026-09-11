package com.warehouse.account.infrastructure.adapter.secondary;

import com.warehouse.account.application.port.secondary.UserInvitationRepository;
import com.warehouse.account.domain.model.UserInvitation;
import com.warehouse.account.domain.vo.UserInvitationId;
import com.warehouse.account.infrastructure.adapter.secondary.entity.UserInvitationEntity;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.Optional;

public class UserInvitationRepositoryImpl implements UserInvitationRepository {

    private final OperatorFilteredRepository<UserInvitationEntity> repository;
    private final NoOperatorContextRepository<UserInvitationEntity> noOperatorContextRepository;

    public UserInvitationRepositoryImpl(
            final OperatorFilteredRepository<UserInvitationEntity> repository,
            final NoOperatorContextRepository<UserInvitationEntity> noOperatorContextRepository) {
        this.repository = repository;
        this.noOperatorContextRepository = noOperatorContextRepository;
    }

    @Override
    public Optional<UserInvitation> findBySupplierId(final SupplierId supplierId) {
        return this.noOperatorContextRepository.createCriteria(UserInvitationEntity.class)
                .eq("supplierId.value", supplierId.value())
                .one()
                .map(this::toModel);
    }

    @Override
    public Optional<UserInvitation> findByTokenHash(final String tokenHash) {
        return this.noOperatorContextRepository.createCriteria(UserInvitationEntity.class)
                .eq("tokenHash", tokenHash)
                .one()
                .map(this::toModel);
    }

    @Override
    public void save(final UserInvitation invitation) {
        final UserInvitationEntity entity = toEntity(invitation);
        if (invitation.getOperatorId() == null) {
            this.repository.create(entity);
        } else {
            entity.assignOperator(invitation.getOperatorId());
            this.repository.update(entity);
        }
    }

    private UserInvitationEntity toEntity(final UserInvitation invitation) {
        return new UserInvitationEntity(
                invitation.getInvitationId().value(),
                invitation.getUserId(),
                invitation.getSupplierId(),
                invitation.getUsername(),
                invitation.getTokenHash(),
                invitation.getCreatedAt(),
                invitation.getExpiresAt(),
                invitation.getAcceptedAt());
    }

    private UserInvitation toModel(final UserInvitationEntity entity) {
        return new UserInvitation(
                new UserInvitationId(entity.getInvitationId()),
                entity.getUserId(),
                entity.getSupplierId(),
                entity.getUsername(),
                entity.getTokenHash(),
                entity.getCreatedAt(),
                entity.getExpiresAt(),
                entity.getAcceptedAt(),
                entity.operatorId());
    }
}
