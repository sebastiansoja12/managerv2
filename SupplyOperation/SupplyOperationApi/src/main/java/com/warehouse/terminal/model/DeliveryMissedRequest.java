package com.warehouse.terminal.model;

import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryMissedRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMissedRequest {

    @XmlElementWrapper(name = "DeliveryMissedDetails")
    @XmlElement(name = "DeliveryMissedDetail")
    private List<DeliveryMissedDetail> deliveryMissedDetails;
}
