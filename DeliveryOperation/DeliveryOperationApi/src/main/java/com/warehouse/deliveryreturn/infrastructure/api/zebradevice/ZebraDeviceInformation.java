package com.warehouse.deliveryreturn.infrastructure.api.zebradevice;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlRootElement(name = "ZebraDeviceInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeviceInformation {

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "ZebraID")
    private Long zebraId;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElement(name = "DepotCode")
    private String depotCode;
}
