package com.warehouse.terminal.request;

import com.warehouse.terminal.model.DeliveryReturnDetail;
import com.warehouse.terminal.enumeration.ProcessType;
import com.warehouse.terminal.information.TerminalDeviceInformation;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @XmlElement(name = "TerminalDeviceInformation")
    private TerminalDeviceInformation terminalDeviceInformation;

    @XmlElement(name = "ParcelID")
    private Long parcelId;

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
