package com.warehouse.zebrareturn.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.response.Response;
import com.warehouse.zebra.infrastructure.api.responsemodel.TerminalResponse;
import org.mapstruct.Mapper;

import com.warehouse.zebrainitialize.model.ZebraInitializeResponse;

@Mapper
public interface ZebraResponseMapper {
    TerminalResponse map(Response response);

    ZebraInitializeResponse mapToInitializeResponse(Response response);
}
