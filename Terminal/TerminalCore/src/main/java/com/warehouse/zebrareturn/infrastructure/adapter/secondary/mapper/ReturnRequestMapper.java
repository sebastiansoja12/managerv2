package com.warehouse.zebrareturn.infrastructure.adapter.secondary.mapper;

import java.util.List;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.request.ReturnRequest;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.api.ReturnPackageRequestDto;
import com.warehouse.zebrareturn.infrastructure.adapter.secondary.api.ReturnRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReturnRequestMapper {

    @Mapping(target = "depotCode.value", source = "zebraDeviceInformation.depotCode")
    @Mapping(target = "username.value", source = "zebraDeviceInformation.username")
    @Mapping(target = "requests", source = "returnRequests")
    ReturnRequestDto map(Request zebraRequest);


    List<ReturnPackageRequestDto> map(List<ReturnRequest> requests);

    @Mapping(target = "supplierCode.value", source = "supplierCode")
    ReturnPackageRequestDto map(ReturnRequest request);
}
