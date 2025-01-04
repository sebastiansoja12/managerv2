package com.warehouse.deliveryreject.domain.service;

import org.springframework.stereotype.Service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliveryreject.domain.model.DeliveryReject;
import com.warehouse.deliveryreject.domain.model.DeliveryRejectDetails;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponseDetails;
import com.warehouse.deliveryreject.domain.vo.Person;
import com.warehouse.deliveryreject.domain.vo.RejectReasonId;

@Service
public class DeliveryRejectConverterServiceImpl implements DeliveryRejectConverterService {


    @Override
    public DeliveryReject convertToDeliveryReject(final DeliveryRejectDetails deliveryReject, final Person recipient) {
        return null;
    }

    @Override
    public DeliveryRejectResponseDetails convertToDeliveryRejectResponseDetails(final DeliveryReject deliveryReject) {
        final RejectReasonId rejectReasonId = deliveryReject.getRejectReasonId();
        final ShipmentId shipmentId = deliveryReject.getShipmentId();
        final ShipmentId newShipmentId = deliveryReject.getShipmentId();
        return null;
    }
}
