package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreject.domain.model.RejectTrackerRequest;
import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.RejectTrackerRequestDto;

@Mapper
public interface RejectTrackerRequestMapper {
    RejectTrackerRequestDto map(final RejectTrackerRequest rejectTrackerRequest);
}
