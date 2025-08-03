package com.warehouse.redirect.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.domain.vo.RedirectParcelRequest;
import com.warehouse.redirect.infrastructure.adapter.primary.api.RedirectParcelRequestDto;
import com.warehouse.redirect.infrastructure.adapter.primary.api.RedirectRequestDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RedirectRequestMapper {
    RedirectRequest map(RedirectRequestDto redirectRequest);

    @Mapping(source = "parcelId.value", target = "parcelId")
    @Mapping(source = "parcel.parcelRelatedId.value", target = "parcel.parcelRelatedId")
    @Mapping(source = "parcel.destination.value", target = "parcel.destination")
    RedirectParcelRequest map(RedirectParcelRequestDto request);
}
