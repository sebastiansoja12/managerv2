package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.ImpersonationConsent;
import com.warehouse.auth.domain.port.secondary.ImpersonationConsentRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.ImpersonationConsentEntity;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PlatformOwnerId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public class ImpersonationConsentRepositoryImpl implements ImpersonationConsentRepository {

    private final ImpersonationConsentReadRepository repository;

    public ImpersonationConsentRepositoryImpl(final ImpersonationConsentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ImpersonationConsent save(final ImpersonationConsent consent) {
        return toModel(repository.save(toEntity(consent)));
    }

    @Override
    public Optional<ImpersonationConsent> findById(final Long consentId) {
        return repository.findById(consentId).map(this::toModel);
    }

    @Override
    public Optional<ImpersonationConsent> findLatest(final PlatformOwnerId platformOwnerId, final OperatorId operatorId) {
        return repository.findFirstByPlatformOwnerIdAndOperatorIdOrderByRequestedAtDesc(platformOwnerId.value(),
                operatorId.value()).map(this::toModel);
    }

    @Override
    public List<ImpersonationConsent> findRecent(final OperatorId operatorId) {
        return repository.findTop50ByOperatorIdOrderByRequestedAtDesc(operatorId.value())
                .stream()
                .map(this::toModel)
                .toList();
    }

    private ImpersonationConsentEntity toEntity(final ImpersonationConsent consent) {
        return new ImpersonationConsentEntity(consent.getId(), consent.getOperatorId().value(),
                consent.getPlatformOwnerId().value(), consent.getPlatformOwnerUsername(), consent.getReason(),
                consent.getStatus(), consent.getRequestedAt(), consent.getRequestExpiresAt(),
                consent.getDecidedBy() != null ? consent.getDecidedBy().value() : null, consent.getDecidedByUsername(),
                consent.getDecidedAt(), consent.getAccessExpiresAt());
    }

    private ImpersonationConsent toModel(final ImpersonationConsentEntity entity) {
        return ImpersonationConsent.restore(entity.getId(), OperatorId.of(entity.getOperatorId()),
                new PlatformOwnerId(entity.getPlatformOwnerId()), entity.getPlatformOwnerUsername(), entity.getReason(),
                entity.getStatus(), entity.getRequestedAt(), entity.getRequestExpiresAt(),
                entity.getDecidedBy() != null ? new UserId(entity.getDecidedBy()) : null, entity.getDecidedByUsername(),
                entity.getDecidedAt(), entity.getAccessExpiresAt());
    }
}
