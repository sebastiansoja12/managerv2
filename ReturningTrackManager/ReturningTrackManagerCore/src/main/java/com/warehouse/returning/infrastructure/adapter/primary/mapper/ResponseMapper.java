package com.warehouse.returning.infrastructure.adapter.primary.mapper;

import com.warehouse.returning.domain.vo.ReturnResponse;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ProcessReturnDto;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnIdDto;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ReturnResponseApi;
import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ShipmentIdDto;

public class ResponseMapper {
	public static ReturnResponseApi toResponseApi(final ReturnResponse res) {
		return new ReturnResponseApi(res.processReturn().stream()
				.map(processReturn -> new ProcessReturnDto(new ShipmentIdDto(processReturn.shipmentId().value()),
						new ReturnIdDto(processReturn.returnId().getValue()), processReturn.processStatus().name()))
                .toList());
	}
}
