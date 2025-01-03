package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;

import com.warehouse.deliveryreject.domain.vo.RejectTrackerRequest;
import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.RejectTrackerRequestDto;

@Mapper
public interface RejectTrackerRequestMapper {
    RejectTrackerRequestDto map(final RejectTrackerRequest rejectTrackerRequest);
}
