package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;

@Mapper
public interface ReturnMapper {

    ReturnEntity map(ReturnPackage returning);

    @Mapping(target = "returnId", source = "id")
    @Mapping(target = "processStatus", source = "returnStatus")
    ProcessReturn map(ReturnEntity returning);

}
