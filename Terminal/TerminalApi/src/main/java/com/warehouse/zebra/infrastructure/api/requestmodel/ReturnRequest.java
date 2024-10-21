package com.warehouse.zebra.infrastructure.api.requestmodel;

import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnRequestType", propOrder = {

})
@XmlRootElement(name = "ReturnRequestInformation")
public class ReturnRequest {

    @XmlElement
    private List<ReturnRequestInformation> requests;

    @XmlElement(name = "Username")
    @XmlSchemaType(name = "normalizedString")
    private String username;

    private String msgType;
}