package com.warehouse.terminal.response;

import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectResponse;
import com.warehouse.terminal.model.DeliveryReturnResponse;

import jakarta.xml.bind.annotation.*;
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

    @XmlElement(name = "DeliveryReturnResponse")
    private DeliveryReturnResponse deliveryReturnResponses;
}