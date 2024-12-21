package com.warehouse.terminal.model;


import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryRejectResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectResponse {

    @XmlElementWrapper(name = "DeliveryRejectResponseDetails")
    @XmlElement(name = "DeliveryRejectResponseDetail")
    private List<DeliveryRejectResponseDetail> deliveryRejectResponseDetails;
}
