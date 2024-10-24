package com.warehouse.redirect.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;

@Mapper
public interface RedirectTokenMapper {
    RedirectTokenEntity map(final RedirectToken redirectToken);
}
