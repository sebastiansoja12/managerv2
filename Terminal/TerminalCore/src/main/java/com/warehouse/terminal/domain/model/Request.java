package com.warehouse.terminal.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.terminal.domain.model.request.ReturnRequest;
import com.warehouse.terminal.domain.model.request.ShipmentCreatedRequest;
import com.warehouse.terminal.domain.vo.DeviceInformation;


public class Request {

    private ProcessType processType;

    private DeviceInformation zebraDeviceInformation;

    private List<ReturnRequest> returnRequests;

    private List<ShipmentCreatedRequest> shipmentCreatedRequests;

	public Request(final ProcessType processType,
                   final DeviceInformation zebraDeviceInformation,
                   final List<ReturnRequest> returnRequests,
                   final List<ShipmentCreatedRequest> shipmentCreatedRequests) {
		this.processType = processType;
		this.zebraDeviceInformation = zebraDeviceInformation;
		this.returnRequests = returnRequests;
		this.shipmentCreatedRequests = shipmentCreatedRequests;
	}

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(final ProcessType processType) {
        this.processType = processType;
    }

    public DeviceInformation getZebraDeviceInformation() {
        return zebraDeviceInformation;
    }

    public void setZebraDeviceInformation(final DeviceInformation zebraDeviceInformation) {
        this.zebraDeviceInformation = zebraDeviceInformation;
    }

    public List<ReturnRequest> getReturnRequests() {
        return returnRequests;
    }

    public void setReturnRequests(final List<ReturnRequest> returnRequests) {
        this.returnRequests = returnRequests;
    }

    public List<ShipmentCreatedRequest> getParcelCreatedRequests() {
        return shipmentCreatedRequests;
    }

    public void setParcelCreatedRequests(final List<ShipmentCreatedRequest> shipmentCreatedRequests) {
        this.shipmentCreatedRequests = shipmentCreatedRequests;
    }
}
