package com.warehouse.zebra.infrastructure.api.dto;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "SupplierCode")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class SupplierCodeDto {

    @XmlElement(name = "Value")
    private String value;
}
