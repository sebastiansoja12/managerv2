package com.warehouse.zebra.infrastructure.adapter.secondary.mapper;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.ReturnRequest;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnPackageRequestDto;
import com.warehouse.zebra.infrastructure.adapter.secondary.api.ReturnRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ReturnRequestMapper {

    @Mapping(target = "depotCode.value", source = "zebraDeviceInformation.depotCode")
    @Mapping(target = "username.value", source = "zebraDeviceInformation.username")
    ReturnRequestDto map(Request zebraRequest);


    List<ReturnPackageRequestDto> map(List<ReturnRequest> requests);

    @Mapping(target = "supplierCode.value", source = "supplierCode")
    ReturnPackageRequestDto map(ReturnRequest request);
}
