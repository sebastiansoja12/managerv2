package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.dto.request.ZebraDeliveryReturnRequest;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DeliveryReturnRequestMapper {


    default DeliveryReturnRequest map(ZebraDeliveryReturnRequest deliveryReturnRequest) {
        return new DeliveryReturnRequest(
                map(deliveryReturnRequest.getZebraDeviceInformation()),
                null, null, null, null
        );
    }

    List<DeliveryReturnRequest> map(List<DeliveryReturnRequestDto> deliveryReturnRequests);

    DeviceInformation map(ZebraDeviceInformation zebraDeviceInformation);
}
