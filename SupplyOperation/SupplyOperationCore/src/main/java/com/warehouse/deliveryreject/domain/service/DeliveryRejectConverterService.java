package com.warehouse.deliveryreject.domain.service;

import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.Person;
import com.warehouse.deliveryreject.domain.vo.ShipmentRejectResponse;

public interface DeliveryRejectConverterService {
    DeliveryReject convertToDeliveryReject(final DeliveryRejectDetails deliveryReject, final Person recipient);
    DeliveryRejectResponseDetails convertToDeliveryRejectResponseDetails(final DeliveryReject deliveryReject,
                                                                         final ShipmentRejectResponse response);
}
