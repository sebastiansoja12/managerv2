package com.warehouse.redirect.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.redirect.domain.model.RedirectResponse;
import com.warehouse.redirect.infrastructure.api.dto.RedirectResponseDto;

@Mapper
public interface RedirectResponseMapper {
    RedirectResponseDto map(RedirectResponse response);
}
