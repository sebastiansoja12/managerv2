package com.warehouse.terminal.model;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

@XmlRootElement(name = "DeliveryReturnRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReturnRequest {

    @XmlElementWrapper(name = "DeliveryReturnDetails")
    @XmlElement(name = "DeliveryReturnDetail")
    private List<DeliveryReturnDetail> deliveryReturnDetails;
}
