package com.warehouse.terminal.response;

import java.util.List;

import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryMissedResponse;
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

    @XmlElementWrapper(name = "DeliveryMissedResponses")
    @XmlElement(name = "DeliveryMissedResponse")
    private List<DeliveryMissedResponse> deliveryMissedResponses;

    @XmlElementWrapper(name = "DeliveryRejectResponses")
    @XmlElement(name = "DeliveryRejectResponse")
    private List<DeliveryRejectResponse> deliveryRejectResponses;

    @XmlElementWrapper(name = "DeliveryReturnResponses")
    @XmlElement(name = "DeliveryReturnResponse")
    private List<DeliveryReturnResponse> deliveryReturnResponses;
}