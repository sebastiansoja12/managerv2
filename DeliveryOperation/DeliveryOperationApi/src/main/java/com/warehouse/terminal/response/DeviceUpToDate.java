package com.warehouse.terminal.response;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeviceUpToDate")
public enum DeviceUpToDate {
    YES, NO
}
