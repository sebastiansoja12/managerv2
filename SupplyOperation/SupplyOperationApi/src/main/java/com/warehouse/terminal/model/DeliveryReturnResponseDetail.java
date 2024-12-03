package com.warehouse.terminal.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "DeliveryReturnResponseDetail")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReturnResponseDetail {

    @XmlElement(name = "SupplierCode")
    private String supplierCode;

    @XmlElement(name = "DepartmentCode")
    private String departmentCode;

    @XmlElement(name = "ShipmentID")
    private Long shipmentId;

    @XmlElement(name = "ReturnToken")
    private String returnToken;
}
