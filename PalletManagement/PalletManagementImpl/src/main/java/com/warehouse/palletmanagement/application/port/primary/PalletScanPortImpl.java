package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.palletmanagement.application.port.primary.command.PalletUnloadScanCommand;
import com.warehouse.palletmanagement.application.port.primary.command.UnloadPalletCommand;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;
import com.warehouse.palletmanagement.application.port.primary.result.PalletUnloadResult;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.application.service.PalletTransportService;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.model.PalletScan;
import com.warehouse.palletmanagement.domain.service.PalletTransportPolicy;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class PalletScanPortImpl implements PalletScanPort {

    private static final Logger log = LoggerFactory.getLogger(PalletScanPortImpl.class);

    private final PalletHandlingManagerServicePort palletHandlingManagerServicePort;
    private final ShipmentServicePort shipmentServicePort;
    private final PalletTransportService palletTransportService;
    private final DomainEventPublisher domainEventPublisher;
    private final OperatorContextProvider operatorContextProvider;

    public PalletScanPortImpl(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                              final ShipmentServicePort shipmentServicePort,
                              final PalletTransportService palletTransportService,
                              final DomainEventPublisher domainEventPublisher,
                              final OperatorContextProvider operatorContextProvider) {
        this.palletHandlingManagerServicePort = palletHandlingManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.palletTransportService = palletTransportService;
        this.domainEventPublisher = domainEventPublisher;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public PalletScanResult scan(final PalletScanCommand command) {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.getByTrackingNumber(command.trackingNumber());
        final PalletScan palletScan = new PalletScan(pallet);
        final Optional<PalletScanErrorCode> rejectionReason = palletScan.rejectionReason();
        if (rejectionReason.isPresent()) {
            log.info("Pallet {} scan rejected: {}", pallet.trackingNumber().value(), rejectionReason.get());
            return PalletScanResult.rejected(pallet, rejectionReason.get());
        }

        final List<ShipmentLoadResult> results = pallet.shipmentIds().stream()
                .map(this.shipmentServicePort::loadOnPallet)
                .toList();

        if (!palletScan.allShipmentsLoaded(results)) {
            final long failedCount = results.stream().filter(result -> !result.loaded()).count();
            log.warn("Pallet {} scan finished with {} of {} shipments not updated",
                    pallet.trackingNumber().value(), failedCount, results.size());
            return PalletScanResult.shipmentsFailed(pallet, palletScan.alreadyLoaded(), results);
        }

        if (palletScan.requiresLoadedNotification(results)) {
            this.domainEventPublisher.publish(new PalletLoaded(palletScan.loadedSnapshot(), Instant.now()));
            log.info("Pallet {} loaded with {} shipments", pallet.trackingNumber().value(), results.size());
        } else {
            log.info("Pallet {} rescanned, already loaded", pallet.trackingNumber().value());
        }
        return PalletScanResult.loaded(pallet, palletScan.alreadyLoaded(), results);
    }

    @Override
    public PalletDetails revertLoading(final PalletId palletId) {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.revertLoading(palletId);
        final long failedCount = pallet.shipmentIds().stream()
                .map(this.shipmentServicePort::revertLoadingFromPallet)
                .filter(result -> !result.loaded())
                .count();
        if (failedCount > 0) {
            log.warn("Pallet {} loading reverted with {} of {} shipments not updated",
                    pallet.trackingNumber().value(), failedCount, pallet.shipmentIds().size());
        }
        this.domainEventPublisher.publish(new PalletLoadingReverted(new PalletLoadingRevertedSnapshot(
                pallet.palletId(), pallet.trackingNumber(), pallet.shipmentIds()), Instant.now()));
        log.info("Pallet {} loading reverted", pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(pallet);
    }

    @Override
    public PalletDetails unload(final UnloadPalletCommand command) {
        return unload(this.palletHandlingManagerServicePort.getPallet(command.palletId()),
                command.missingShipmentIds()).pallet();
    }

    @Override
    public PalletUnloadResult unloadScanned(final PalletUnloadScanCommand command) {
        return unload(this.palletHandlingManagerServicePort.getByTrackingNumber(command.trackingNumber()), List.of());
    }

    private PalletUnloadResult unload(final PalletDetails current, final List<ShipmentId> missingShipmentIds) {
        if (current.palletStatus() == PalletStatus.DELIVERED) {
            log.info("Pallet {} already unloaded", current.trackingNumber().value());
            return PalletUnloadResult.alreadyUnloaded(this.palletTransportService.withTransport(current));
        }
        final DepartmentId unloadDepartmentId = this.operatorContextProvider.currentContext()
                .map(OperatorDetails::departmentId)
                .orElse(current.destinationDepartmentId());
        PalletTransportPolicy.ensureUnloadable(current, unloadDepartmentId, missingShipmentIds);

        final PalletDetails pallet = this.palletHandlingManagerServicePort.unload(current.palletId());
        final List<ShipmentId> unloadedShipmentIds = pallet.shipmentIds().stream()
                .filter(shipmentId -> !missingShipmentIds.contains(shipmentId))
                .toList();
        final List<ShipmentLoadResult> results = unloadedShipmentIds.stream()
                .map(this.shipmentServicePort::unloadFromPallet)
                .toList();
        final long failedCount = results.stream().filter(result -> !result.loaded()).count();
        if (failedCount > 0) {
            log.warn("Pallet {} unloaded with {} of {} shipments not updated",
                    pallet.trackingNumber().value(), failedCount, unloadedShipmentIds.size());
        }
        this.domainEventPublisher.publish(new PalletUnloaded(new PalletUnloadedSnapshot(pallet.palletId(),
                pallet.trackingNumber(), unloadedShipmentIds, missingShipmentIds, unloadDepartmentId),
                Instant.now()));
        log.info("Pallet {} unloaded with {} shipments, {} missing", pallet.trackingNumber().value(),
                unloadedShipmentIds.size(), missingShipmentIds.size());
        return PalletUnloadResult.unloaded(this.palletTransportService.withTransport(pallet), results);
    }
}
