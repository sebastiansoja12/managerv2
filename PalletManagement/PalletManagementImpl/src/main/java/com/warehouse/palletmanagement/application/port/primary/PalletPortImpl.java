package com.warehouse.palletmanagement.application.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.application.port.primary.command.AddPalletShipmentsCommand;
import com.warehouse.palletmanagement.application.port.primary.command.CreatePalletCommand;
import com.warehouse.palletmanagement.application.port.secondary.CarrierServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletHandlingManagerServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.application.service.PalletDocumentService;
import com.warehouse.palletmanagement.application.service.PalletGrossWeightService;
import com.warehouse.palletmanagement.application.service.PalletTransportService;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.model.PalletizableShipmentPolicy;
import com.warehouse.palletmanagement.domain.service.PalletHistoryMerger;
import com.warehouse.palletmanagement.domain.service.PalletTransportPolicy;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentFile;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.palletmanagement.domain.vo.PalletSearchCriteria;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.domain.vo.PalletUpdate;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class PalletPortImpl implements PalletPort {

    private static final Logger log = LoggerFactory.getLogger(PalletPortImpl.class);

    private static final String CARRIER_UNAVAILABLE = "CARRIER_UNAVAILABLE";

    private final PalletHandlingManagerServicePort palletHandlingManagerServicePort;
    private final ShipmentServicePort shipmentServicePort;
    private final CarrierServicePort carrierServicePort;
    private final PalletTransportService palletTransportService;
    private final PalletGrossWeightService palletGrossWeightService;
    private final PalletDocumentService palletDocumentService;

    public PalletPortImpl(final PalletHandlingManagerServicePort palletHandlingManagerServicePort,
                          final ShipmentServicePort shipmentServicePort,
                          final CarrierServicePort carrierServicePort,
                          final PalletTransportService palletTransportService,
                          final PalletGrossWeightService palletGrossWeightService,
                          final PalletDocumentService palletDocumentService) {
        this.palletHandlingManagerServicePort = palletHandlingManagerServicePort;
        this.shipmentServicePort = shipmentServicePort;
        this.carrierServicePort = carrierServicePort;
        this.palletTransportService = palletTransportService;
        this.palletGrossWeightService = palletGrossWeightService;
        this.palletDocumentService = palletDocumentService;
    }

    @Override
    public PalletDetails createEmptyPallet() {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.createEmptyPallet();
        log.info("Empty pallet {} created", pallet.trackingNumber().value());
        return pallet;
    }

    @Override
    public PalletDetails createPallet(final CreatePalletCommand command) {
        ensurePalletizable(command.shipmentIds());
        final PalletDetails pallet = this.palletHandlingManagerServicePort.createPallet(command.shipmentIds());
        log.info("Pallet {} created with {} shipments", pallet.trackingNumber().value(), pallet.shipmentIds().size());
        return this.palletGrossWeightService.recalculate(pallet);
    }

    @Override
    public PalletDetails getPallet(final PalletTrackingNumber trackingNumber) {
        return this.palletTransportService.withTransport(
                this.palletHandlingManagerServicePort.getByTrackingNumber(trackingNumber));
    }

    @Override
    public PalletDetails getPallet(final PalletId palletId) {
        return this.palletTransportService.withTransport(this.palletHandlingManagerServicePort.getPallet(palletId));
    }

    @Override
    public PalletPage findPallets(final PalletSearchCriteria criteria) {
        return this.palletTransportService.withTransport(this.palletHandlingManagerServicePort.findPallets(criteria));
    }

    @Override
    public PalletDetails addShipments(final AddPalletShipmentsCommand command) {
        ensurePalletizable(command.shipmentIds());
        final PalletDetails pallet = this.palletHandlingManagerServicePort.addShipments(command.palletId(),
                command.shipmentIds());
        log.info("{} shipments added to pallet {}", command.shipmentIds().size(), pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(this.palletGrossWeightService.recalculate(pallet));
    }

    @Override
    public PalletDetails removeShipment(final PalletId palletId, final ShipmentId shipmentId) {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.removeShipment(palletId, shipmentId);
        log.info("Shipment {} removed from pallet {}", shipmentId.getValue(), pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(this.palletGrossWeightService.recalculate(pallet));
    }

    @Override
    public PalletDetails defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight) {
        if (maxGrossWeight == null || maxGrossWeight.signum() <= 0) {
            throw new IllegalArgumentException("Max gross weight must be positive");
        }
        final PalletDetails pallet = this.palletHandlingManagerServicePort.defineMaxGrossWeight(palletId, maxGrossWeight);
        log.info("Max gross weight {} KG set for pallet {}", maxGrossWeight, pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(pallet);
    }

    @Override
    public PalletDetails updatePallet(final PalletId palletId, final PalletUpdate update) {
        if (update == null) {
            throw new IllegalArgumentException("Pallet update is required");
        }
        final PalletDetails pallet = this.palletHandlingManagerServicePort.updatePallet(palletId, update);
        log.info("Pallet {} attributes updated", pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(pallet);
    }

    @Override
    public List<PalletHistoryEntry> getHistory(final PalletId palletId) {
        return PalletHistoryMerger.merge(this.palletHandlingManagerServicePort.getHistory(palletId),
                this.palletTransportService.history(palletId));
    }

    @Override
    public PalletDetails changeDepartments(final PalletId palletId, final PalletDepartmentsChange departmentsChange) {
        return this.palletTransportService.withTransport(
                this.palletHandlingManagerServicePort.changeDepartments(palletId, departmentsChange));
    }

    @Override
    public PalletDetails assignCarrier(final PalletId palletId, final PalletCarrierAssignment assignment) {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.getPallet(palletId);
        PalletTransportPolicy.ensureCarrierChangeable(pallet);
        final TransportCarrier carrier = this.carrierServicePort.findActiveCarrier(assignment.carrierId())
                .orElseThrow(() -> new PalletRequestRejectedException(409, CARRIER_UNAVAILABLE,
                        "Carrier " + assignment.carrierId().getValue() + " is not available"));
        final PalletCarrier palletCarrier = PalletTransportPolicy.assign(carrier, assignment, Instant.now());
        log.info("Carrier {} assigned to pallet {}", carrier.carrierId().getValue(), pallet.trackingNumber().value());
        return this.palletTransportService.assign(pallet, palletCarrier);
    }

    @Override
    public PalletDetails changeStatus(final PalletId palletId, final PalletStatus targetStatus) {
        PalletTransportPolicy.ensureManualStatusChange(targetStatus, this.palletTransportService.hasCarrier(palletId));
        final PalletDetails pallet = this.palletHandlingManagerServicePort.changeStatus(palletId, targetStatus);
        log.info("Pallet {} status changed to {}", pallet.trackingNumber().value(), pallet.palletStatus());
        return this.palletTransportService.withTransport(pallet);
    }

    @Override
    public PalletDetails cancelPallet(final PalletId palletId) {
        final PalletDetails pallet = this.palletHandlingManagerServicePort.cancelPallet(palletId);
        log.info("Pallet {} cancelled", pallet.trackingNumber().value());
        return this.palletTransportService.withTransport(pallet);
    }

    @Override
    public PalletDocumentFile generateLabel(final PalletTrackingNumber trackingNumber) {
        return this.palletDocumentService.label(getPallet(trackingNumber));
    }

    @Override
    public PalletDocumentFile generateTransportDocument(final PalletTrackingNumber trackingNumber) {
        return this.palletDocumentService.transportDocument(getPallet(trackingNumber));
    }

    private void ensurePalletizable(final List<ShipmentId> shipmentIds) {
        shipmentIds.forEach(shipmentId -> PalletizableShipmentPolicy.ensurePalletizable(shipmentId,
                this.shipmentServicePort.findShipment(shipmentId)));
    }
}
