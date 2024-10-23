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

import java.util.Objects;

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
    @Mapping(target = "shipmentId", source = "parcelId")
    ProcessReturn map(ReturnEntity returning);


    @Mapping(target = "reason", source = "returnInformationEntity.reason")
    @Mapping(target = "shipmentId", source = "parcelId")
    ReturnModel mapToReturnModel(ReturnEntity returnEntity);

    default Long map(ReturnId returnId) {
        if (Objects.nonNull(returnId)) {
            return returnId.getValue();
        }
        throw new IllegalStateException("Return Id is null");
    }

    com.warehouse.returning.domain.model.ReturnStatus map(ReturnStatus returnStatus);
}
