package com.warehouse.deliveryreturn.infrastructure.adapter.primary.mapper;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.model.DeviceInformation;
import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.information.TerminalDeviceInformation;
import com.warehouse.terminal.request.TerminalRequest;

@Mapper
public interface DeliveryReturnRequestMapper {


    default DeliveryReturnRequest map(TerminalRequest request) {
        return new DeliveryReturnRequest(map(request.getProcessType()), map(request.getTerminalDeviceInformation()),
                map(request.getDeliveryReturnDetails()));
    }

    List<DeliveryReturnDetails> map(List<DeliveryReturnDetail> deliveryReturnRequests);

    @Mapping(target = "token", ignore = true)
    DeliveryReturnDetails map(DeliveryReturnDetail deliveryReturnDetail);

    DeviceInformation map(TerminalDeviceInformation terminalDeviceInformation);

    ProcessType map(com.warehouse.terminal.enumeration.ProcessType processType);
}
