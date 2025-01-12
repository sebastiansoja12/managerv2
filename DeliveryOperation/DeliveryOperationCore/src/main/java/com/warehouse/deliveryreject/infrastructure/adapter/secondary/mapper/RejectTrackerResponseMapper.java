package com.warehouse.deliveryreject.infrastructure.adapter.secondary.mapper;


import java.util.UUID;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreject.domain.vo.RejectTrackerResponse;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.ProcessIdDto;
import com.warehouse.deliveryreject.infrastructure.adapter.secondary.api.RejectTrackerResponseDto;

@Mapper
public interface RejectTrackerResponseMapper {
    RejectTrackerResponse map(final RejectTrackerResponseDto rejectTrackerResponse);

    UUID map(final ProcessIdDto value);
}
