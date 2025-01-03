package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import com.warehouse.deliveryreject.domain.vo.RejectTrackerRequest;
import com.warehouse.deliveryreject.domain.port.secondary.RejectTrackerServicePort;
import com.warehouse.deliveryreject.domain.vo.RejectTrackerResponse;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper.RejectTrackerRequestMapper;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper.RejectTrackerResponseMapper;
import lombok.extern.slf4j.Slf4j;

import static org.mapstruct.factory.Mappers.getMapper;

@Slf4j
public class RejectTrackerServiceAdapter implements RejectTrackerServicePort {

    private final RejectTrackerRequestMapper requestMapper = getMapper(RejectTrackerRequestMapper.class);

    private final RejectTrackerResponseMapper responseMapper = getMapper(RejectTrackerResponseMapper.class);

    @Override
    public RejectTrackerResponse logRejectInTracker(final RejectTrackerRequest rejectTrackerRequest) {
        return null;
    }
}
