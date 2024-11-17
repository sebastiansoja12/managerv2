package com.warehouse.terminal.request;

import java.util.List;

import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.information.Device;
import com.warehouse.terminal.model.DeliveryRejectRequest;
import com.warehouse.terminal.model.DeliveryReturnDetail;

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
     * DeliveryRejectRequests.
     */
    @XmlElementWrapper(name = "DeliveryRejectRequests")
    @XmlElement(name = "DeliveryRejectRequest")
    private List<DeliveryRejectRequest> deliveryRejectRequests;

    /**
     * -- GETTER --
     *  DeliverReturnDetails.
     *
     * @return
     */
    @XmlElementWrapper(name = "DeliveryReturnDetails")
    @XmlElement(name = "DeliveryReturnDetail")
    private List<DeliveryReturnDetail> deliveryReturnDetails;
}
