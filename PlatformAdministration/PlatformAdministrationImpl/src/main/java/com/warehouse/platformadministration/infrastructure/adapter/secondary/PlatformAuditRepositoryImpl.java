package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.PlatformAuditRepository;
import com.warehouse.platformadministration.domain.model.PlatformAuditEntry;
import com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper.PlatformAuditEntryMapper;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class PlatformAuditRepositoryImpl implements PlatformAuditRepository {

    private final PlatformAuditReadRepository repository;

    public PlatformAuditRepositoryImpl(final PlatformAuditReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(final PlatformAuditEntry platformAuditEntry) {
        repository.save(PlatformAuditEntryMapper.toEntity(platformAuditEntry));
    }

    @Override
    public List<PlatformAuditEntry> findLatest(final OperatorId operatorId, final int limit) {
        final PageRequest page = PageRequest.of(0, limit);
        return (operatorId == null
                ? repository.findAllByOrderByCreatedAtDesc(page)
                : repository.findAllByOperatorIdOrderByCreatedAtDesc(operatorId.value(), page))
                .stream()
                .map(PlatformAuditEntryMapper::toModel)
                .toList();
    }
}
