package com.warehouse.palletmanagement.application.service;

import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.domain.model.PalletCargoWeight;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PalletGrossWeightService {

    private static final Logger log = LoggerFactory.getLogger(PalletGrossWeightService.class);

    private final PalletHandlingManagerServicePort palletHandlingManagerServicePort;
    private final ShipmentServicePort shipmentServicePort;

    public PalletGrossWeightService(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                                    final ShipmentServicePort shipmentServicePort) {
        this.palletHandlingManagerServicePort = palletHandlingManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
    }

    public PalletDetails recalculate(final PalletDetails pallet) {
        final List<PalletShipment> shipments = pallet.shipmentIds().stream()
                .map(this.shipmentServicePort::findShipment)
                .flatMap(Optional::stream)
                .toList();
        if (shipments.size() < pallet.shipmentIds().size()) {
            log.warn("Pallet {} weight calculated without {} unreadable shipments", pallet.trackingNumber().value(),
                    pallet.shipmentIds().size() - shipments.size());
        }
        final PalletWeight grossWeight = PalletCargoWeight.of(shipments);
        try {
            final PalletDetails updatedPallet = this.palletHandlingManagerServicePort.changeGrossWeight(
                    pallet.palletId(), grossWeight);
            if (updatedPallet.blockReason().filter(PalletLoadBlockReason.OVERWEIGHT::equals).isPresent()) {
                log.warn("Pallet {} exceeds max gross weight with {} KG", pallet.trackingNumber().value(),
                        grossWeight.value());
            }
            return updatedPallet;
        } catch (final PalletServiceUnavailableException exception) {
            log.warn("Gross weight of pallet {} could not be updated: {}", pallet.trackingNumber().value(),
                    exception.getMessage());
            return pallet;
        }
    }
}
