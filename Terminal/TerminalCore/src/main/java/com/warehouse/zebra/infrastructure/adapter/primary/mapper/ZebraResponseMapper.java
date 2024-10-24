package com.warehouse.zebra.infrastructure.adapter.primary.mapper;

import com.warehouse.zebra.infrastructure.api.responsemodel.TerminalResponse;
import org.mapstruct.Mapper;

import com.warehouse.commonassets.response.Response;

@Mapper
public interface ZebraResponseMapper {
    TerminalResponse map(final Response response);
}
