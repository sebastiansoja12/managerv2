package com.warehouse.deliveryreturn.infrastructure.api.response;


import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "ZebraDeliveryReturnResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeliveryReturnResponse {

    @XmlElementWrapper(name = "DeliveryReturnResponses")
	@XmlElement(name = "DeliveryReturnResponse")
    private List<DeliveryReturnResponseDetail> deliveryReturnResponses;

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DepotCode")
    private String depotCode;
}
