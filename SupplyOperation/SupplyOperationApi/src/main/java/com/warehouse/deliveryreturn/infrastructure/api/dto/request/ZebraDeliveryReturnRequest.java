package com.warehouse.deliveryreturn.infrastructure.api.dto.request;


import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnRequestDto;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "ZebraDeliveryReturnRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZebraDeliveryReturnRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    @XmlElement(name = "ZebraDeviceInformation")
    private ZebraDeviceInformation zebraDeviceInformation;

    @XmlElementWrapper(name = "DeliveryReturnRequests")
    @XmlElement(name = "DeliveryReturnRequest")
    private List<DeliveryReturnRequestDto> deliveryReturnRequests;
}
