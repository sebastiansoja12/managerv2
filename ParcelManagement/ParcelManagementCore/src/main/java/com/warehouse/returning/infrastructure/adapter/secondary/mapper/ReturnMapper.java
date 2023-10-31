package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
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


    @Mapping(target = "reason", constant = "TBA")
    ReturnModel mapToReturnModel(ReturnEntity returnEntity);

    default Long map(ReturnId returnId) {
        return returnId.getValue();
    }

}
