package com.warehouse.terminal.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "DeliveryRejectDetail")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRejectDetail {

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DeliveryStatus")
    private String deliveryStatus;

    @XmlElement(name = "DepartmentCode")
    private String departmentCode;

    @XmlElement(name = "RejectReason")
    private String rejectReason;
}
