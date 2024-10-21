package com.warehouse.zebrainitialize.model;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "ParcelCreatedRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelCreatedRequest {

    @XmlElement(name = "ParcelID")
    private Long parcelId;

    @XmlElement(name = "ParcelRelatedID")
    private Long parcelRelatedId;
}
