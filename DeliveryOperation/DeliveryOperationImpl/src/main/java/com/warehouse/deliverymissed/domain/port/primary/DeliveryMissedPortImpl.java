package com.warehouse.deliverymissed.domain.port.primary;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.deliverymissed.domain.enumeration.DeliveryInstruction;
import com.warehouse.deliverymissed.domain.model.DeliveryMissed;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedDetails;
import com.warehouse.deliverymissed.domain.model.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.service.SuggestedActionDetermineService;
import com.warehouse.deliverymissed.domain.vo.*;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.id.DeliveryMissedDetailId;

public class DeliveryMissedPortImpl implements DeliveryMissedPort {

    private final DeliveryMissedService deliveryMissedService;

    private final DeliveryInstructionServicePort deliveryInstructionServicePort;

    private final SuggestedActionDetermineService suggestedActionDetermineService;

    public DeliveryMissedPortImpl(final DeliveryMissedService deliveryMissedService,
                                  final DeliveryInstructionServicePort deliveryInstructionServicePort,
                                  final SuggestedActionDetermineService suggestedActionDetermineService) {
        this.deliveryMissedService = deliveryMissedService;
        this.deliveryInstructionServicePort = deliveryInstructionServicePort;
        this.suggestedActionDetermineService = suggestedActionDetermineService;
    }

    @Override
    public DeliveryMissedResponse process(final DeliveryMissedRequest request) {
        final DeliveryId deliveryId = determineDeliveryId(request);
        final List<DeliveryMissed> deliveryMisses = new ArrayList<>();
        final Map<ShipmentId, DeliveryMissedInstruction> deliveryMissedInstructions =
                this.deliveryInstructionServicePort.getDeliveryInstruction(deliveryId);

        for (final DeliveryMissedDetails deliveryMissedDetail : request.getDeliveryMissedDetails()) {
            final ShipmentId shipmentId = deliveryMissedDetail.getShipmentId();
            final DeliveryAttemptNumber deliveryAttemptNumber = this.deliveryMissedService.countDeliveryAttempts(shipmentId);
            final DeliveryMissedDetailId deliveryMissedDetailId = this.deliveryMissedService.nextId();

            final DeliveryMissedInstruction instruction = deliveryMissedInstructions.get(shipmentId);

            final DeliveryMissed deliveryMissed = DeliveryMissed.builder()
                    .deliveryMissedDetailId(deliveryMissedDetailId)
                    .shipmentId(shipmentId)
                    .deliveryAttemptNumber(deliveryAttemptNumber)
                    .addressChanged(instruction.deliveryInstruction() == DeliveryInstruction.NEW_ADDRESS)
                    .incidentReport(new IncidentReport(instruction.instruction()))
                    .plannedDeliveryDate(Instant.now())
                    .newProposedDate(instruction.proposedDate())
                    .created(Instant.now())
                    .build();

            deliveryMisses.add(deliveryMissed);
        }

        final List<DeliveryMissedResponseDetails> deliveryMissedResponseDetails = this.suggestedActionDetermineService
                .determineSuggestedActions(deliveryMissedInstructions, deliveryMisses);

        return DeliveryMissedResponse.builder()
                .deliveryMissedResponseDetails(deliveryMissedResponseDetails)
                .deviceInformation(request.getDeviceInformation())
                .build();
    }

    private DeliveryId determineDeliveryId(final DeliveryMissedRequest request) {
        return null;
    }

}
