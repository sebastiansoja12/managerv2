package com.warehouse.terminal.request;

import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectRequest;

import com.warehouse.terminal.model.DeliveryReturnRequest;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "TerminalRequest")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerminalRequest {

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    /**
     * -- GETTER --
     *  Terminal device information.
     *
     * @return
     */
    @XmlElement(name = "Device")
    private Device device;

    /**
     * -- GETTER
     * DeliveryRejectRequest.
     */
    @XmlElement(name = "DeliveryRejectRequest")
    private DeliveryRejectRequest deliveryRejectRequest;

    /**
     * -- GETTER
     * DeliveryRejectRequest.
     */
    @XmlElement(name = "DeliveryReturnRequest")
    private DeliveryReturnRequest deliveryReturnRequest;

}
