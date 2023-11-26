package com.warehouse.deliveryreturn.domain.service;

import com.warehouse.deliveryreturn.domain.model.DeliveryReturnDetails;
import com.warehouse.deliveryreturn.domain.model.DeliveryReturnRequest;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturn;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponse;

import java.util.List;
import java.util.Set;

public interface DeliveryReturnService {
    List<DeliveryReturn> deliverReturn(Set<DeliveryReturnDetails> deliveryReturnRequests);
}
