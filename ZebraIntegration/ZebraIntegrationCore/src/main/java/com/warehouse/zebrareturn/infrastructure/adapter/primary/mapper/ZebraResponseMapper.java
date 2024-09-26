package com.warehouse.zebrareturn.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.response.Response;
import org.mapstruct.Mapper;

import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;
import com.warehouse.zebrainitialize.model.ZebraInitializeResponse;

@Mapper
public interface ZebraResponseMapper {
    ZebraResponse map(Response response);

    ZebraInitializeResponse mapToInitializeResponse(Response response);
}
