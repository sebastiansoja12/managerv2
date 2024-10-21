package com.warehouse.zebra.domain.vo;

import java.util.List;

import com.warehouse.commonassets.vo.DeviceInformation;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Request {

    ProcessType processType;

    DeviceInformation zebraDeviceInformation;

    List<ReturnRequest> returnRequests;

    List<ParcelCreatedRequest> parcelCreatedRequests;
}
