package com.warehouse.terminal.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryRejectResponseDetail")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectResponseDetail {

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DepartmentCode")
    private String departmentCode;

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "ShipmentID")
    private Long newShipmentId;

    @XmlElement(name = "DeliveryStatus")
    private String deliveryStatus;

    @XmlElement(name = "RejectReason")
    private String rejectReason;
}
