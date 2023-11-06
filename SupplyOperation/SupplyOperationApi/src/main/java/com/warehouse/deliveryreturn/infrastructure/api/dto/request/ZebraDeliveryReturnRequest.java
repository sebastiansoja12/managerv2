package com.warehouse.deliveryreturn.infrastructure.api.dto.request;


import java.util.List;

import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetail;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @XmlElementWrapper(name = "DeliveryReturnDetails")
    @XmlElement(name = "DeliveryReturnDetail")
    private List<DeliveryReturnDetail> deliveryReturnDetails;
}
