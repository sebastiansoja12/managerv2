package com.warehouse.deliverymissed.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.model.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInstruction;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponseDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SuggestedActionDetermineServiceImpl implements SuggestedActionDetermineService {
    

    @Override
	public List<DeliveryMissedResponseDetails> determineSuggestedActions(
			final Map<ShipmentId, DeliveryMissedInstruction> deliveryMissedInstruction,
			final List<DeliveryMissed> deliveryMisses) {
        return List.of();
    }
}
