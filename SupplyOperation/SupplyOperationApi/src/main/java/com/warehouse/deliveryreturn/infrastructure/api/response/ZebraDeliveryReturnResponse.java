package com.warehouse.deliveryreturn.infrastructure.api.response;


import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "ZebraDeliveryReturnResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZebraDeliveryReturnResponse {

	@XmlElementWrapper(name = "DeliveryReturnResponses")
	@XmlElement(name = "DeliveryReturnResponse")
    private List<DeliveryReturnResponseDetail> deliveryReturnResponses;

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DepotCode")
    private String depotCode;
}
