package com.warehouse.redirect.infrastructure.adapter.primary.mapper;

import com.warehouse.redirect.domain.model.RedirectParcelResponse;
import com.warehouse.redirect.infrastructure.adapter.primary.api.ParcelResponseDto;
import org.mapstruct.Mapper;

import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.infrastructure.adapter.primary.api.RedirectResponseDto;
import org.mapstruct.Mapping;

@Mapper
public interface RedirectResponseMapper {
    RedirectResponseDto map(RedirectResponse response);

    @Mapping(source = "parcelId", target = "parcelId.value")
    ParcelResponseDto map(RedirectParcelResponse response);
}
