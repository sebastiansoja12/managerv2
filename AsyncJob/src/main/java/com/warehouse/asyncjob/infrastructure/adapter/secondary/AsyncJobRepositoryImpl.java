package com.warehouse.asyncjob.infrastructure.adapter.secondary;

import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.infrastructure.adapter.secondary.mapper.AsyncJobEntityMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public class AsyncJobRepositoryImpl implements AsyncJobRepository {

    private final AsyncJobJpaRepository repository;
    private final AsyncJobEntityMapper mapper;

    public AsyncJobRepositoryImpl(final AsyncJobJpaRepository repository, final AsyncJobEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public AsyncJob save(final AsyncJob job) {
        return this.mapper.toDomain(this.repository.save(this.mapper.toEntity(job)));
    }

    @Override
    public Optional<AsyncJob> findById(final AsyncJobId id) {
        return this.repository.findById(id.value()).map(this.mapper::toDomain);
    }

    @Override
    public List<AsyncJob> findLatestByOperatorId(final OperatorId operatorId, final int limit) {
        return this.repository.findAllByOperatorIdOrderByCreatedAtDesc(operatorId.getValue(), PageRequest.of(0, limit))
                .stream()
                .map(this.mapper::toDomain)
                .toList();
    }
}
