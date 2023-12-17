package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.returntoken.domain.vo.Token;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.TokenDto;

@Mapper
public interface ReturnTokenResponseMapper {

    @Mapping(target = "token", source = "value")
    TokenDto map(Token token);
}
