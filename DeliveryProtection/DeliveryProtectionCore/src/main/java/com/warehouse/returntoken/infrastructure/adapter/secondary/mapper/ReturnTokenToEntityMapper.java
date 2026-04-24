package com.warehouse.returntoken.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenEntity;

@Mapper
public interface ReturnTokenToEntityMapper {

    ReturnTokenEntity map(final ReturnToken returnToken);
}
