package com.warehouse.zebra.infrastructure.adapter.secondary.mapper;

import java.util.List;

import com.warehouse.commonassets.request.ReturnRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.request.Request;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnPackageRequestDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnRequestDto;

@Mapper
public interface ReturnRequestMapper {

    @Mapping(target = "depotCode.value", source = "zebraDeviceInformation.depotCode")
    @Mapping(target = "username.value", source = "zebraDeviceInformation.username")
    @Mapping(target = "requests", source = "returnRequests")
    ReturnRequestDto map(final Request zebraRequest);


    List<ReturnPackageRequestDto> map(final List<ReturnRequest> requests);

    @Mapping(target = "supplierCode.value", source = "supplierCode")
    ReturnPackageRequestDto map(final ReturnRequest request);
}
