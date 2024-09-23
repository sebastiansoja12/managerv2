package com.warehouse.commonassets.request;

import java.util.List;

import com.warehouse.commonassets.vo.DeviceInformation;
import com.warehouse.commonassets.enumeration.ProcessType;


public class Request {

    private ProcessType processType;

    private DeviceInformation zebraDeviceInformation;

    private List<ReturnRequest> returnRequests;

    private List<ParcelCreatedRequest> parcelCreatedRequests;

	public Request(ProcessType processType, DeviceInformation zebraDeviceInformation,
			List<ReturnRequest> returnRequests, List<ParcelCreatedRequest> parcelCreatedRequests) {
		this.processType = processType;
		this.zebraDeviceInformation = zebraDeviceInformation;
		this.returnRequests = returnRequests;
		this.parcelCreatedRequests = parcelCreatedRequests;
	}

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public DeviceInformation getZebraDeviceInformation() {
        return zebraDeviceInformation;
    }

    public void setZebraDeviceInformation(DeviceInformation zebraDeviceInformation) {
        this.zebraDeviceInformation = zebraDeviceInformation;
    }

    public List<ReturnRequest> getReturnRequests() {
        return returnRequests;
    }

    public void setReturnRequests(List<ReturnRequest> returnRequests) {
        this.returnRequests = returnRequests;
    }

    public List<ParcelCreatedRequest> getParcelCreatedRequests() {
        return parcelCreatedRequests;
    }

    public void setParcelCreatedRequests(List<ParcelCreatedRequest> parcelCreatedRequests) {
        this.parcelCreatedRequests = parcelCreatedRequests;
    }
}
