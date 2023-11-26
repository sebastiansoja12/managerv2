package com.warehouse.deliveryreturn.infrastructure.api.response;


import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UpdateStatus")
public enum UpdateStatus {
    OK, NOT_OK, ERROR
}
