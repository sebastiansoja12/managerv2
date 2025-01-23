package com.warehouse.terminal.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryMissedResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMissedResponse {

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "DeliveryID")
    private String deliveryId;
}
