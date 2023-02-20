package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RequestToEntityMapper {


    UserEntity map(RegisterRequest registerRequest);

}
