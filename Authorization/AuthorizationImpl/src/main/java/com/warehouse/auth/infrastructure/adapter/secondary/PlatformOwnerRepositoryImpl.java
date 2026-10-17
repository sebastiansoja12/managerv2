package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.PlatformOwner;
import com.warehouse.auth.domain.port.secondary.PlatformOwnerRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.PlatformOwnerToEntityMapper;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.PlatformOwnerToModelMapper;
import com.warehouse.commonassets.identificator.PlatformOwnerId;

import java.util.List;
import java.util.Optional;

public class PlatformOwnerRepositoryImpl implements PlatformOwnerRepository {

    private final PlatformOwnerReadRepository repository;

    public PlatformOwnerRepositoryImpl(final PlatformOwnerReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PlatformOwner> findByUsername(final String username) {
        return repository.findByUsername(username)
                .map(PlatformOwnerToModelMapper::map);
    }

    @Override
    public Optional<PlatformOwner> findById(final PlatformOwnerId platformOwnerId) {
        return repository.findById(platformOwnerId)
                .map(PlatformOwnerToModelMapper::map);
    }

    @Override
    public List<PlatformOwner> findAll() {
        return repository.findAll()
                .stream()
                .map(PlatformOwnerToModelMapper::map)
                .toList();
    }

    @Override
    public void save(final PlatformOwner platformOwner) {
        repository.save(PlatformOwnerToEntityMapper.map(platformOwner));
    }

    @Override
    public PlatformOwnerId nextPlatformOwnerId() {
        return new PlatformOwnerId(repository.findMaxPlatformOwnerIdValue() + 1);
    }

    @Override
    public boolean existsByUsernameOrEmail(final String username, final String email) {
        return repository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public long countActive() {
        return repository.countByDeletedFalse();
    }
}
