package com.warehouse.terminal.request;

import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryMissedRequest;
import com.warehouse.terminal.model.DeliveryRejectRequest;
import com.warehouse.terminal.model.DeliveryReturnRequest;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
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
     * DeliveryReturnRequest.
     */
    @XmlElement(name = "DeliveryReturnRequest")
    private DeliveryReturnRequest deliveryReturnRequest;

    /**
     * -- GETTER
     * DeliveryMissedRequest.
     */
    @XmlElement(name = "DeliveryMissedRequest")
    private DeliveryMissedRequest deliveryMissedRequest;

}
