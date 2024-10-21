package com.warehouse.zebrainitialize.model;

import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "ZebraInitializeResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZebraInitializeResponse {

    @XmlElement(name = "ZebraID")
    private Long zebraId;

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElementWrapper(name = "RouteProcesses")
    @XmlElement(name = "RouteProcess")
    private List<RouteProcess> routeProcesses;
}
