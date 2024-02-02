package com.warehouse.zebra.infrastructure.adapter.primary.mapper;

import com.warehouse.zebra.domain.vo.Response;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;
import com.warehouse.zebrainitialize.model.ZebraInitializeResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ZebraResponseMapper {
    ZebraResponse map(Response response);

    ZebraInitializeResponse mapToInitializeResponse(Response response);
}
