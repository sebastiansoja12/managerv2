package com.warehouse.terminal.model;

import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryRejectRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectRequest {

    @XmlElementWrapper(name = "DeliveryRejectDetails")
    @XmlElement(name = "DeliveryRejectDetail")
    private List<DeliveryRejectDetail> deliveryRejectDetails;

}