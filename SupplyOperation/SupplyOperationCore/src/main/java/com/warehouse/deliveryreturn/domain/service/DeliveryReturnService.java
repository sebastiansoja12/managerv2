package com.warehouse.deliveryreturn.domain.service;

import java.util.List;
import java.util.Set;

import com.warehouse.delivery.domain.vo.DeviceInformation;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;

public interface DeliveryReturnService {
    List<DeliveryReturn> deliverReturn(final Set<DeliveryReturnDetails> deliveryReturnRequests, final DeviceInformation deviceInformation);
}
