package com.warehouse.zebrareturn.infrastructure.adapter.primary.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.request.Request;
import com.warehouse.commonassets.request.ReturnRequest;
import com.warehouse.zebra.infrastructure.api.requestmodel.ReturnRequestInformation;
import com.warehouse.zebra.infrastructure.api.requestmodel.TerminalRequest;



@Mapper
public interface ZebraRequestMapper {

    Request map(final TerminalRequest terminalRequest);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    ReturnRequest map(final ReturnRequestInformation returnRequestInformation);
}
