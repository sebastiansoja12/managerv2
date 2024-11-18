package com.warehouse.zebra.infrastructure.api.responsemodel;

import com.warehouse.zebra.infrastructure.api.dto.ProcessReturn;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement(name = "TerminalResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalResponse {

    @XmlElement(name = "TerminalID")
    private Long terminalId;

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElementWrapper(name = "ProcessReturns")
    @XmlElement(name = "ProcessReturn")
    private List<ProcessReturn> processReturns;

    @XmlElementWrapper(name = "RouteProcesses")
    @XmlElement(name = "RouteProcess")
    private List<RouteProcessType> routeProcesses;
}
