package com.warehouse.zebra.infrastructure.api.requestmodel;


import java.util.List;

import com.warehouse.zebrainitialize.model.ShipmentRequest;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlRootElement(name = "TerminalRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    @XmlElement(name = "TerminalDeviceInformation")
    private TerminalDeviceInformation terminalDeviceInformation;

    @XmlElementWrapper(name = "ReturnRequestInformations")
    @XmlElement(name = "ReturnRequestInformation")
    private List<ReturnRequestInformation> returnRequests;

    @XmlElementWrapper(name = "ShipmentRequests")
    @XmlElement(name = "ShipmentRequest")
    private List<ShipmentRequest> shipmentRequests;
}
