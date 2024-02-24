package com.warehouse.zebra.infrastructure.api.responsemodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LogStatus")
@XmlAccessorType(XmlAccessType.FIELD)
public enum LogStatus {
    OK, NOT_OK, ERROR;
}
