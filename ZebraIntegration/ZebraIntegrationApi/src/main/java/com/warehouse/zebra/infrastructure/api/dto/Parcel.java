package com.warehouse.zebra.infrastructure.api.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Parcel")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parcel {

    @XmlElement(name = "ParcelID")
    private Long id;

    @XmlElement(name = "ParcelSize")
    private SizeDto parcelSize;

    @XmlElement(name = "Destination")
    private String destination;

    @XmlElement(name = "ParcelStatus")
    private StatusDto status;

    @XmlElement(name = "ParcelType")
    private ParcelTypeDto parcelType;

    @XmlElement(name = "ParcelRelatedId")
    private Long parcelRelatedId;
}
