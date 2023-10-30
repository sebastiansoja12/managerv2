package com.warehouse.zebra.infrastructure.adapter.primary.mapper;


import com.warehouse.zebra.domain.vo.Request;
import com.warehouse.zebra.domain.vo.ReturnRequest;
import com.warehouse.zebra.infrastructure.api.requestmodel.ReturnRequestInformation;
import com.warehouse.zebra.infrastructure.api.requestmodel.ZebraRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ZebraRequestMapper {
    Request map(ZebraRequest zebraRequest);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    ReturnRequest map(ReturnRequestInformation returnRequestInformation);
}
