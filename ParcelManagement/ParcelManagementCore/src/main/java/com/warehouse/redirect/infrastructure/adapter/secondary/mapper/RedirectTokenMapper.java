package com.warehouse.redirect.infrastructure.adapter.secondary.mapper;

import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RedirectTokenMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "expiryDate", ignore = true)
    RedirectTokenEntity map(RedirectToken redirectToken);
}
