package com.warehouse.onboarding.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.onboarding.application.port.secondary.UserOnboardingRepository;
import com.warehouse.onboarding.domain.model.UserOnboarding;
import com.warehouse.onboarding.domain.vo.ConsentVersion;
import com.warehouse.onboarding.domain.vo.UserConsent;
import com.warehouse.onboarding.infrastructure.adapter.secondary.entity.UserConsentEmbeddable;
import com.warehouse.onboarding.infrastructure.adapter.secondary.entity.UserOnboardingEntity;

import java.util.Optional;

public class UserOnboardingRepositoryImpl implements UserOnboardingRepository {

    private final OperatorFilteredRepository<UserOnboardingEntity> repository;

    public UserOnboardingRepositoryImpl(final OperatorFilteredRepository<UserOnboardingEntity> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserOnboarding> findByUserId(final UserId userId) {
        return findEntity(userId).map(this::toModel);
    }

    @Override
    public void save(final UserOnboarding onboarding) {
        final UserOnboardingEntity entity = toEntity(onboarding);
        if (findEntity(onboarding.getUserId()).isPresent()) {
            entity.assignOperator(onboarding.getOperatorId());
            this.repository.update(entity);
        } else {
            this.repository.create(entity);
        }
    }

    private Optional<UserOnboardingEntity> findEntity(final UserId userId) {
        return this.repository.createCriteria(UserOnboardingEntity.class)
                .eq("userId.value", userId.value())
                .one();
    }

    private UserOnboardingEntity toEntity(final UserOnboarding onboarding) {
        return new UserOnboardingEntity(
                onboarding.getUserId(),
                onboarding.getConsents().stream()
                        .map(consent -> new UserConsentEmbeddable(consent.type(), consent.version().value(),
                                consent.acceptedAt()))
                        .toList(),
                onboarding.getTutorialStatus(),
                onboarding.getTutorialFinishedAt());
    }

    private UserOnboarding toModel(final UserOnboardingEntity entity) {
        return new UserOnboarding(
                entity.getUserId(),
                entity.getConsents().stream()
                        .map(consent -> new UserConsent(consent.getConsentType(),
                                new ConsentVersion(consent.getConsentVersion()), consent.getAcceptedAt()))
                        .toList(),
                entity.getTutorialStatus(),
                entity.getTutorialFinishedAt(),
                entity.operatorId());
    }
}
