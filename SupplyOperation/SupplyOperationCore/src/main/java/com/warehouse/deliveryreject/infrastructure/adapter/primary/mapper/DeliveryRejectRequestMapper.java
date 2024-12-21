package com.warehouse.deliveryreject.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.delivery.dto.DeviceInformationDto;
import com.warehouse.delivery.dto.ProcessTypeDto;
import com.warehouse.delivery.dto.UsernameDto;
import com.warehouse.delivery.dto.VersionDto;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectRequest;
import com.warehouse.deliveryreject.dto.DeliveryRejectDetailsDto;
import com.warehouse.deliveryreject.dto.request.DeliveryRejectRequestDto;

@Mapper
public interface DeliveryRejectRequestMapper {

    default DeliveryRejectRequest map(final DeliveryRejectRequestDto deliveryRejectRequest) {
        final List<DeliveryRejectDetails> deliveryRejectDetails = map(deliveryRejectRequest.getDeliveryRejectDetails());
        final DeviceInformation deviceInformation = map(deliveryRejectRequest.getDeviceInformation());
        final ProcessType processType = map(deliveryRejectRequest.getProcessType());
        return new DeliveryRejectRequest(deliveryRejectDetails, deviceInformation, processType);
    }

    List<DeliveryRejectDetails> map(final List<DeliveryRejectDetailsDto> deliveryRejectDetails);

    @Mapping(target = "username", source = "username.value")
    @Mapping(target = "version", source = "version.value")
    DeviceInformation map(final DeviceInformationDto deviceInformation);

    ProcessType map(final ProcessTypeDto processTypeDto);

    String map(final VersionDto value);

    String map(final UsernameDto username);

}
