package com.warehouse.terminal.response;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "TerminalResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalResponse {

    @XmlElement(name = "ZebraID")
    private Long terminalId;

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElement(name = "ParcelID")
    private Long parcelId;

    @XmlElement(name = "DeliveryID")
    private Long deliveryId;
}