package com.warehouse.delivery.domain.model;

import java.util.List;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.vo.DeviceInformation;


public class Request {

    private ProcessType processType;

    private DeviceInformation deviceInformation;

    private List<DeliveryRejectRequest> deliveryRejectRequests;

    private List<DeliveryMissedRequest> deliveryMissedRequests;
}
