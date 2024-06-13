package com.warehouse.terminal.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "DeliveryReturnDetail")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryReturnDetail {

    @XmlElement(name = "ParcelID")
    private Long parcelId;

    @XmlElement(name = "DeliveryStatus")
    private DeliveryStatus deliveryStatus;

    @XmlElement(name = "DepotCode")
    private String depotCode;

    @XmlElement(name = "SupplierCode")
    private String supplierCode;
}
