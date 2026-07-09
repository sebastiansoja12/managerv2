package com.warehouse.deliverymissed.domain.service;

import java.util.List;
import java.util.Map;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.model.DeliveryMissed;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedInstruction;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponseDetails;

public interface SuggestedActionDetermineService {
    List<DeliveryMissedResponseDetails> determineSuggestedActions(final Map<ShipmentId, DeliveryMissedInstruction> deliveryMissedInstruction,
                                                                  final List<DeliveryMissed> deliveryMisses);
}
