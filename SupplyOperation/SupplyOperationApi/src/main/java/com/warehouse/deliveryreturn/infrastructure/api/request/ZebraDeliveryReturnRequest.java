package com.warehouse.deliveryreturn.infrastructure.api.request;


import java.util.List;

import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetail;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "ZebraDeliveryReturnRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeliveryReturnRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    /**
     * -- GETTER --
     *  Zebra device information.
     *
     * @return
     */
    @XmlElement(name = "ZebraDeviceInformation")
    private ZebraDeviceInformation zebraDeviceInformation;

    /**
     * -- GETTER --
     *  DeliverReturnDetails.
     *
     * @return
     */
    @XmlElementWrapper(name = "DeliveryReturnDetails")
    @XmlElement(name = "DeliveryReturnDetail")
    private List<DeliveryReturnDetail> deliveryReturnDetails;
}
