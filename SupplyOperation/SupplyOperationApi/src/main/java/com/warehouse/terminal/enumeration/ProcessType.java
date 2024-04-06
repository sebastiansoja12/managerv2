package com.warehouse.terminal.enumeration;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProcessType")
public enum ProcessType {
    CREATED, RETURN, ROUTE, REJECT, REROUTE, REDIRECT, MISS
}

