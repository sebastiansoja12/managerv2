package com.warehouse.terminal.request;

import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.information.TerminalDeviceInformation;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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

    @XmlElement(name = "ParcelID")
    private Long parcelId;
}
