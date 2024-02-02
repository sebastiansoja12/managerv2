package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Request {

    ProcessType processType;

    DeviceInformation zebraDeviceInformation;

    List<ReturnRequest> returnRequests;

    List<ParcelCreatedRequest> parcelCreatedRequests;
}
