package com.warehouse.zebrainitialize.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "ShipmentRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "ShipmentRelatedID")
    private Long shipmentRelatedId;
}
