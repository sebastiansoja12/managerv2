package com.warehouse.zebra.infrastructure.api.responsemodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "RouteProcess")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteProcessType {

    @XmlElement(name = "ParcelID")
    private Long parcelId;

    @XmlElement(name = "LogStatus")
    private LogStatus logStatus;
}
