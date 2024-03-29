package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnIdDto;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnPackageRequestDto;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturningRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.domain.vo.ReturnId;


@Mapper
public interface ReturnRequestMapper {

    @Mapping(target = "depotCode", source = "depotCode.value")
    @Mapping(target = "username", source = "username.value")
    ReturnRequest map(ReturningRequestDto returningRequest);

    @Mapping(target = "supplierCode", source = "supplierCode.value")
    ReturnPackageRequest map(ReturnPackageRequestDto request);

    ReturnId map(ReturnIdDto id);
}
