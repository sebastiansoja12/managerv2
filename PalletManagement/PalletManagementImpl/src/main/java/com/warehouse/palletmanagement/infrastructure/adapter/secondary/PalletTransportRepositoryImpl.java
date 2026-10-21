package com.warehouse.palletmanagement.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;
import com.warehouse.palletmanagement.application.port.secondary.PalletTransportRepository;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.entity.PalletTransportEntity;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper.PalletTransportEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PalletTransportRepositoryImpl implements PalletTransportRepository {

    private static final Logger log = LoggerFactory.getLogger(PalletTransportRepositoryImpl.class);

    private final OperatorFilteredRepository<PalletTransportEntity> repository;
    private final OperatorContextProvider operatorContextProvider;
    private final CurrentUserApiService currentUserApiService;

    public PalletTransportRepositoryImpl(final OperatorFilteredRepository<PalletTransportEntity> repository,
                                         final OperatorContextProvider operatorContextProvider,
                                         final CurrentUserApiService currentUserApiService) {
        this.repository = repository;
        this.operatorContextProvider = operatorContextProvider;
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    @Transactional
    public PalletCarrier append(final PalletId palletId, final PalletTrackingNumber trackingNumber,
                                final PalletCarrier carrier) {
        final OperatorDetails context = this.operatorContextProvider.currentContext()
                .orElseThrow(() -> new IllegalStateException("Operator context is required"));
        final PalletTransportEntity entity = PalletTransportEntityMapper.toEntity(palletId, trackingNumber, carrier,
                context.userId(), currentUsername(), context.departmentId());
        this.repository.create(entity);
        return PalletTransportEntityMapper.toModel(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PalletCarrier> findCurrent(final PalletId palletId) {
        return this.repository.createCriteria(PalletTransportEntity.class)
                .eq("palletId.value", palletId.value())
                .desc("assignedAt")
                .maxResults(1)
                .one()
                .map(PalletTransportEntityMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<PalletId, PalletCarrier> findCurrent(final Collection<PalletId> palletIds) {
        final Map<PalletId, PalletCarrier> result = new LinkedHashMap<>();
        if (palletIds == null || palletIds.isEmpty()) {
            return result;
        }
        this.repository.createCriteria(PalletTransportEntity.class)
                .in("palletId.value", palletIds.stream().map(PalletId::value).toList())
                .desc("assignedAt")
                .list()
                .forEach(entity -> result.putIfAbsent(entity.getPalletId(), PalletTransportEntityMapper.toModel(entity)));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PalletCarrier> findHistory(final PalletId palletId) {
        return this.repository.createCriteria(PalletTransportEntity.class)
                .eq("palletId.value", palletId.value())
                .desc("assignedAt")
                .list()
                .stream()
                .map(PalletTransportEntityMapper::toModel)
                .toList();
    }

    private String currentUsername() {
        try {
            final UserDto user = this.currentUserApiService.getCurrentUser();
            return user == null ? null : user.username();
        } catch (final RuntimeException exception) {
            log.debug("Current user could not be resolved: {}", exception.getMessage());
            return null;
        }
    }
}
