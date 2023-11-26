package com.warehouse.returning.infrastructure.adapter.secondary.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnInformationEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;

@Mapper
public interface ReturnMapper {


    @Mapping(target = "parcelId", source = "parcel.id")
    ReturnEntity map(ReturnPackageRequest returning);

    @AfterMapping
    default void attachReturnInformation(@MappingTarget ReturnEntity returnEntity, ReturnPackageRequest returnPackage) {
        final ReturnInformationEntity returnInformationEntity = new ReturnInformationEntity();
        returnInformationEntity.setReturnStatus(returnEntity.getReturnStatus());
        returnInformationEntity.setReason(returnPackage.getReason());
        returnEntity.attachReturnInformation(returnInformationEntity);
    }

    ReturnStatus map(com.warehouse.returning.domain.model.ReturnStatus returnStatus);

    @Mapping(target = "returnId", source = "id")
    @Mapping(target = "processStatus", source = "returnStatus")
    ProcessReturn map(ReturnEntity returning);


    @Mapping(target = "reason", source = "returnInformationEntity.reason")
    ReturnModel mapToReturnModel(ReturnEntity returnEntity);

    default Long map(ReturnId returnId) {
        return returnId.getValue();
    }

    com.warehouse.returning.domain.model.ReturnStatus map(ReturnStatus returnStatus);
}
