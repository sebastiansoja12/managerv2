package com.warehouse.deliverymissed.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryMissedDetailsRepository;

public class DeliveryMissedDetailsRepositoryImpl implements DeliveryMissedDetailsRepository {
    
    private final DeliveryMissedDetailsReadRepository deliveryMissedDetailsReadRepository;

	public DeliveryMissedDetailsRepositoryImpl(
			final DeliveryMissedDetailsReadRepository deliveryMissedDetailsReadRepository) {
		this.deliveryMissedDetailsReadRepository = deliveryMissedDetailsReadRepository;
	}
    
    @Override
    public Integer count(final ShipmentId shipmentId) {
        return deliveryMissedDetailsReadRepository.countByShipmentId(shipmentId);
    }
}
