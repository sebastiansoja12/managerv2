package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.entity.AsyncJobEntity;

@Repository
public class AsyncJobRepositoryImpl implements AsyncJobRepository {

    private final AsyncJobJpaRepository repository;

    public AsyncJobRepositoryImpl(final AsyncJobJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public AsyncJob save(final AsyncJob job) {
        return this.repository.save(AsyncJobEntity.from(job)).toDomain();
    }

    @Override
    public Optional<AsyncJob> findById(final UUID id) {
        return this.repository.findById(id).map(AsyncJobEntity::toDomain);
    }
}
