package com.warehouse.deliveryreturn.infrastructure.api.dto.request;


import java.util.List;

import com.warehouse.deliveryreturn.infrastructure.api.dto.DeliveryReturnDetail;
import com.warehouse.deliveryreturn.infrastructure.api.zebradevice.ZebraDeviceInformation;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "ZebraDeliveryReturnRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeliveryReturnRequest {

	public ZebraDeliveryReturnRequest(ProcessType processType, ZebraDeviceInformation zebraDeviceInformation,
			List<DeliveryReturnDetail> deliveryReturnDetails) {
		this.processType = processType;
		this.zebraDeviceInformation = zebraDeviceInformation;
		this.deliveryReturnDetails = deliveryReturnDetails;
	}

    public ProcessType getProcessType() {
        return processType;
    }

    @XmlElement(name = "ProcessType")
    private ProcessType processType;

    @XmlElement(name = "ZebraDeviceInformation")
    private ZebraDeviceInformation zebraDeviceInformation;

    @XmlElementWrapper(name = "DeliveryReturnDetails")
    @XmlElement(name = "DeliveryReturnDetail")
    private List<DeliveryReturnDetail> deliveryReturnDetails;

    /**
     * Process Type.
     * @param processType
     */
    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    /**
     * Zebra device information.
     * @return
     */
    public ZebraDeviceInformation getZebraDeviceInformation() {
        return zebraDeviceInformation;
    }

    /**
     * ZebraDeviceInformation.
     * @param zebraDeviceInformation
     */
    public void setZebraDeviceInformation(ZebraDeviceInformation zebraDeviceInformation) {
        this.zebraDeviceInformation = zebraDeviceInformation;
    }

    /**
     * DeliverReturnDetails.
     * @return
     */
    public List<DeliveryReturnDetail> getDeliveryReturnDetails() {
        return deliveryReturnDetails;
    }

    /**
     * Set DeliveryReturnDetails
     * @param deliveryReturnDetails
     */
    public void setDeliveryReturnDetails(List<DeliveryReturnDetail> deliveryReturnDetails) {
        this.deliveryReturnDetails = deliveryReturnDetails;
    }
}
