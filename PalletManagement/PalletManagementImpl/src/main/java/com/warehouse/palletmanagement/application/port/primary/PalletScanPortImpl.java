package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.palletmanagement.application.port.primary.command.PalletScanCommand;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletScanErrorCode;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.model.PalletScan;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
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
    private final DomainEventPublisher domainEventPublisher;

    public PalletScanPortImpl(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                              final ShipmentServicePort shipmentServicePort,
                              final DomainEventPublisher domainEventPublisher) {
        this.palletHandlingManagerServicePort = palletHandlingManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.domainEventPublisher = domainEventPublisher;
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
}
