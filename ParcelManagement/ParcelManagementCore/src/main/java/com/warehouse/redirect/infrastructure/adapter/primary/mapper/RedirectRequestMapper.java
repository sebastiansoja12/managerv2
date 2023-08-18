package com.warehouse.redirect.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.redirect.domain.model.RedirectRequest;
import com.warehouse.redirect.infrastructure.api.dto.RedirectRequestDto;

@Mapper
public interface RedirectRequestMapper {
    RedirectRequest map(RedirectRequestDto redirectRequest);
}
