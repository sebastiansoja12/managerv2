package com.warehouse.zebra.infrastructure.api.requestmodel;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "TerminalDeviceInformation")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalDeviceInformation {

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "TerminalId")
    private Long terminalId;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElement(name = "DepotCode")
    private String depotCode;

}
