package com.warehouse.deliveryreturn.domain.service;

import java.util.List;
import java.util.Set;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.terminal.DeviceInformation;

public interface DeliveryReturnService {
    List<DeliveryReturn> deliverReturn(final Set<DeliveryReturnDetails> deliveryReturnRequests, final DeviceInformation deviceInformation);
}
