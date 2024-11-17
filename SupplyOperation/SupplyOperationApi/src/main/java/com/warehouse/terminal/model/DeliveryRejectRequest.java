package com.warehouse.terminal.model;

import com.warehouse.terminal.enumeration.DeliveryStatus;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryRejectRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectRequest {

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "DeliveryStatus")
    private DeliveryStatus deliveryStatus;

    @XmlElement(name = "RejectReason")
    private String rejectReason;

}