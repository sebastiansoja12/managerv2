package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;

@Mapper
public interface ReturnStatusMapper {

    ReturnStatus map(com.warehouse.returning.domain.model.ReturnStatus returnStatus);
}
