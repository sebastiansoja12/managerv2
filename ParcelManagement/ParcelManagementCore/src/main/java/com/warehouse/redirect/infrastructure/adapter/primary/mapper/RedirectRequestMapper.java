package com.warehouse.redirect.infrastructure.adapter.primary.mapper;

import com.warehouse.redirect.domain.model.RedirectParcel;
import com.warehouse.redirect.infrastructure.api.dto.ParcelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.redirect.domain.model.RedirectParcelRequest;
import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.infrastructure.api.dto.RedirectParcelRequestDto;
import com.warehouse.redirect.infrastructure.api.dto.RedirectRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RedirectRequestMapper {
    RedirectRequest map(RedirectRequestDto redirectRequest);

    @Mapping(source = "parcelId.value", target = "parcelId")
    RedirectParcelRequest map(RedirectParcelRequestDto request);

    RedirectParcel map(ParcelDto value);
}
