package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnRequest;
import com.warehouse.returning.infrastructure.api.dto.ReturnPackageRequestDto;
import com.warehouse.returning.infrastructure.api.dto.ReturningRequestDto;
import org.mapstruct.Mapper;

@Mapper
public interface ReturnRequestMapper {
    ReturnRequest map(ReturningRequestDto returningRequest);

    ReturnPackageRequest map(ReturnPackageRequestDto request);
}
