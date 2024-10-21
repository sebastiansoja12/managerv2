package com.warehouse.zebra.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.response.Response;
import com.warehouse.zebra.infrastructure.api.responsemodel.ZebraResponse;

@Mapper
public interface ZebraResponseMapper {
    ZebraResponse map(final Response response);
}
