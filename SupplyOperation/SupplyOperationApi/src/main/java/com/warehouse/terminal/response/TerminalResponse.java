package com.warehouse.terminal.response;

import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectResponse;

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

    @XmlElement(name = "Device")
    private Device device;
//
//    @XmlElementWrapper(name = "DeliveryMissedResponses")
//    @XmlElement(name = "DeliveryMissedResponse")
//    private List<DeliveryMissedResponse> deliveryMissedResponses;

    @XmlElement(name = "DeliveryRejectResponse")
    private DeliveryRejectResponse deliveryRejectResponse;

//    @XmlElementWrapper(name = "DeliveryReturnResponses")
//    @XmlElement(name = "DeliveryReturnResponse")
//    private List<DeliveryReturnResponse> deliveryReturnResponses;
}