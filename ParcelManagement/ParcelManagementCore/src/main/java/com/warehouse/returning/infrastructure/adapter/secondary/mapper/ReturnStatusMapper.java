package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;
import org.mapstruct.Mapper;

@Mapper
public interface ReturnStatusMapper {

    ReturnStatus map(com.warehouse.returning.domain.model.ReturnStatus returnStatus);
}
