package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.shipment.domain.vo.FaultDescription;
import com.warehouse.shipment.domain.vo.ReturnCode;
import com.warehouse.shipment.domain.vo.RouteLogRecord;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetail;
import com.warehouse.shipment.domain.vo.RouteLogRecordDetails;
import com.warehouse.shipment.domain.vo.TerminalId;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.FaultDescriptionDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.ReturnCodeDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecordDetailDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecordDetailsDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.api.RouteLogRecordDto;

public class RouteLogRecordMapper {

	public RouteLogRecord map(final RouteLogRecordDto dto) {
		if (dto == null) {
			return null;
		}

		return new RouteLogRecord(
				dto.processId(),
				dto.shipmentId(),
				map(dto.routeLogRecordDetails()),
				map(dto.returnCode()),
				map(dto.faultDescription())
		);
	}

	private RouteLogRecordDetails map(final RouteLogRecordDetailsDto dto) {
		if (dto == null) {
			return null;
		}

		final Set<RouteLogRecordDetail> details = dto.routeLogRecordDetailSet() == null
				? Set.of()
				: dto.routeLogRecordDetailSet().stream()
				.map(this::map)
				.collect(Collectors.toSet());

		return new RouteLogRecordDetails(details);
	}

	private RouteLogRecordDetail map(final RouteLogRecordDetailDto dto) {
		if (dto == null) {
			return null;
		}

		return new RouteLogRecordDetail(
				dto.id(),
				dto.terminalId() == null ? null : new TerminalId(dto.terminalId().value()),
				dto.version(),
				dto.username(),
				dto.supplierCode(),
				dto.departmentCode(),
				dto.shipmentStatus(),
				dto.description(),
				dto.timestamp(),
				dto.processType(),
				dto.request()
		);
	}

	private ReturnCode map(final ReturnCodeDto dto) {
		return dto == null ? null : new ReturnCode(dto.value());
	}

	private FaultDescription map(final FaultDescriptionDto dto) {
		return dto == null ? null : new FaultDescription(dto.value());
	}
}
