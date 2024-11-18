package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;


import com.warehouse.deliveryreject.domain.vo.RejectTrackerResponse;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.RejectTrackerResponseDto;
import org.mapstruct.Mapper;

@Mapper
public interface RejectTrackerResponseMapper {
    RejectTrackerResponse map(final RejectTrackerResponseDto rejectTrackerResponse);
}
