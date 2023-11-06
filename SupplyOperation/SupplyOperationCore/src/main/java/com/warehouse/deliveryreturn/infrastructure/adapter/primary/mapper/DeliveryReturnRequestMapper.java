package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.deliveryreturn.domain.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetail;
import com.warehouse.deliveryreturn.infrastructure.api.dto.request.ZebraDeliveryReturnRequest;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;

@Mapper
public interface DeliveryReturnRequestMapper {


    default DeliveryReturnRequest map(ZebraDeliveryReturnRequest request) {
        return new DeliveryReturnRequest(map(request.getProcessType()), map(request.getZebraDeviceInformation()),
                map(request.getDeliveryReturnDetails()));
    }

    List<DeliveryReturnDetails> map(List<DeliveryReturnDetail> deliveryReturnRequests);

    @Mapping(target = "token", ignore = true)
    DeliveryReturnDetails map(DeliveryReturnDetail deliveryReturnDetail);

    DeviceInformation map(ZebraDeviceInformation zebraDeviceInformation);

    ProcessType map(com.warehouse.deliveryreturn.infrastructure.api.dto.request.ProcessType processType);
}
