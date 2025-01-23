package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteDetails;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.DeliveryReturnRouteRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.routelog.ProcessType;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.routelog.request.DeliveryReturnLogRequestDto;

@Mapper
public interface DeliveryRouteRequestMapper {
    default List<DeliveryReturnLogRequestDto> map(DeliveryReturnRouteRequest request) {
        return request.getDeliveryReturnRouteDetails()
                .stream()
                .map(e -> mapToDeliveryLogRequest(e, request))
                .collect(Collectors.toList());
    }
    
	default DeliveryReturnLogRequestDto mapToDeliveryLogRequest(DeliveryReturnRouteDetails returnRouteDetails,
			DeliveryReturnRouteRequest returnRouteRequest) {
        return DeliveryReturnLogRequestDto.builder()
                .deliveryStatus(returnRouteDetails.getDeliveryStatus())
                .id(returnRouteDetails.getId())
                .processType(ProcessType.RETURN)
                .updateStatus(returnRouteDetails.getUpdateStatus())
                .parcelId(returnRouteDetails.getParcelId())
                .username(returnRouteRequest.getUsername())
                .depotCode(returnRouteRequest.getDepotCode())
                .build();
    }
}
