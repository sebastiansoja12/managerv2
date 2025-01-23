package com.warehouse.terminal.model;


import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement(name = "DeliveryReturnResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReturnResponse {
    
	@XmlElementWrapper(name = "DeliveryReturnResponseDetails")
	@XmlElement(name = "DeliveryReturnResponseDetail")
	private List<DeliveryReturnResponseDetail> deliveryReturnResponseDetails;
}
