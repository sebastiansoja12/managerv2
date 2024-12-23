package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.returntoken.domain.model.ReturnPackageRequest;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;
import com.warehouse.returntoken.domain.vo.Supplier;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnPackageRequestDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.ReturnTokenRequestDto;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.SupplierDto;

@Mapper
public interface ReturnTokenRequestMapper {

    default ReturnTokenRequest map(final ReturnTokenRequestDto returnTokenRequest) {
        final Supplier supplier = map(returnTokenRequest.getSupplier());
        final List<ReturnPackageRequest> returnPackageRequests = returnTokenRequest.getReturnPackageRequests()
                .stream()
                .map(this::map)
                .toList();
        return new ReturnTokenRequest(returnPackageRequests, supplier);
    }

    ReturnPackageRequest map(final ReturnPackageRequestDto returnPackageRequestDto);

    Supplier map(final SupplierDto supplier);
}
