package com.warehouse.logistics.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.logistics.domain.port.secondary.PalletScanServicePort;
import com.warehouse.logistics.domain.vo.PalletScanCommand;
import com.warehouse.logistics.domain.vo.PalletScanDetail;
import com.warehouse.logistics.domain.vo.PalletScanOutcome;
import com.warehouse.palletmanagement.api.PalletApiService;
import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.process.ProcessHubEventPublisher;
import com.warehouse.process.infrastructure.event.ProcessCommunicationEvent;

import java.time.LocalDateTime;

public class PalletScanServiceAdapter implements PalletScanServicePort {

    private final PalletApiService palletApiService;
    private final ProcessHubEventPublisher processHubEventPublisher;

    public PalletScanServiceAdapter(final PalletApiService palletApiService,
                                    final ProcessHubEventPublisher processHubEventPublisher) {
        this.palletApiService = palletApiService;
        this.processHubEventPublisher = processHubEventPublisher;
    }

    @Override
    public PalletScanOutcome scan(final PalletScanCommand command) {
        final PalletScanResponseDto response = this.palletApiService.scan(
                new PalletScanRequestDto(command.trackingNumber()));
        this.processHubEventPublisher.publish(new ProcessCommunicationEvent(
                command.processId(),
                ServiceType.LOGISTICS_ORCHESTRATOR,
                ServiceType.PALLET_HANDLING_MANAGER,
                ProcessType.PALLET_SCAN,
                LocalDateTime.now(),
                "PalletScanRequest{trackingNumber=%s}".formatted(command.trackingNumber()),
                "PalletScanResponse{palletId=%s, status=%s, success=%s, shipments=%s}".formatted(
                        response.palletId(), response.palletStatus(), response.success(), response.shipments().size()),
                response.success() ? null : response.errorMessage()));
        return new PalletScanOutcome(
                response.trackingNumber(),
                response.palletId(),
                response.palletStatus(),
                response.success(),
                response.alreadyLoaded(),
                response.errorCode(),
                response.errorMessage(),
                response.shipments().stream()
                        .map(shipment -> new PalletScanDetail(shipment.shipmentId(), shipment.success(),
                                shipment.shipmentStatus(), shipment.errorMessage()))
                        .toList());
    }
}
