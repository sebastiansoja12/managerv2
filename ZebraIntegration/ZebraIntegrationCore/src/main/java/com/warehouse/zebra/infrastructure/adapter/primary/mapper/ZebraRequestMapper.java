package com.warehouse.zebra.infrastructure.adapter.primary.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.ReturnRequest;
import com.warehouse.zebra.infrastructure.api.requestmodel.ReturnRequestInformation;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraRequest;

@Mapper
public interface ZebraRequestMapper {

    @Mapping(target = "returnRequests", source = "requests")
    Request map(ZebraRequest zebraRequest);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    ReturnRequest map(ReturnRequestInformation returnRequestInformation);
}
