package com.warehouse.pallet.domain.port.primary;

import com.warehouse.pallet.domain.model.PalletUpdate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.warehouse.pallet.domain.enumeration.PalletHistoryEventType;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;
import com.warehouse.pallet.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.pallet.domain.port.secondary.PalletHistoryRepository;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.domain.model.Weight;

import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.exception.PalletModificationException;
import com.warehouse.pallet.domain.exception.PalletNotFoundException;
import com.warehouse.pallet.domain.exception.PalletStatusTransitionException;
import com.warehouse.pallet.domain.model.AssignDriverRequest;
import com.warehouse.pallet.domain.model.Driver;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletLoadedRequest;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.model.SealNumberRequest;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.service.SealNumberService;
import com.warehouse.pallet.domain.vo.DriverId;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PalletPortImpl implements PalletPort {

    private static final Logger log = LoggerFactory.getLogger(PalletPortImpl.class);

    private final PalletStorageService palletStorageService;

    private final DriverStorageService driverStorageService;

    private final SealNumberService sealNumberService;

    private final PalletHistoryRepository palletHistoryRepository;

    private final CurrentUserServicePort currentUserServicePort;

    public PalletPortImpl(final PalletStorageService palletStorageService,
                          final DriverStorageService driverStorageService,
                          final SealNumberService sealNumberService,
                          final PalletHistoryRepository palletHistoryRepository,
                          final CurrentUserServicePort currentUserServicePort) {
        this.palletStorageService = palletStorageService;
        this.driverStorageService = driverStorageService;
        this.sealNumberService = sealNumberService;
        this.palletHistoryRepository = palletHistoryRepository;
        this.currentUserServicePort = currentUserServicePort;
    }

    @Override
    public Pallet createEmptyPallet(final OperatorId operatorId, final DepartmentId originDepartmentId) {
        final PalletId palletId = this.palletStorageService.nextPalletId();
        final TrackingNumber trackingNumber = this.palletStorageService.nextTrackingNumber();
        return record(this.palletStorageService.createEmptyPallet(palletId, trackingNumber, operatorId,
                originDepartmentId), PalletHistoryEventType.CREATED, null);
    }

    @Override
    public Pallet createPalletWithShipments(final OperatorId operatorId, final DepartmentId originDepartmentId,
                                            final Set<ShipmentId> shipmentIds) {
        final PalletId palletId = this.palletStorageService.nextPalletId();
        final TrackingNumber trackingNumber = this.palletStorageService.nextTrackingNumber();
        return record(this.palletStorageService.createPalletWithShipments(palletId, trackingNumber, operatorId,
                originDepartmentId, shipmentIds), PalletHistoryEventType.CREATED, shipmentIdsDetails(shipmentIds));
    }

    @Override
    public void createPallet(final Pallet pallet) {

    }

    @Override
    public void updatePallet(final Pallet pallet) {

    }

    @Override
    public void deletePallet(final PalletId palletId) {

    }

    @Override
    public Pallet getPallet(final PalletId palletId, final OperatorId operatorId) {
        return ensureOwnership(this.palletStorageService.find(palletId), operatorId);
    }

    @Override
    public Pallet getPalletByTrackingNumber(final TrackingNumber trackingNumber, final OperatorId operatorId) {
        return ensureOwnership(this.palletStorageService.findByTrackingNumber(trackingNumber), operatorId);
    }

    @Override
    public PalletPage findPallets(final PalletSearchCriteria criteria) {
        return this.palletStorageService.search(criteria);
    }

    @Override
    public Pallet cancelPallet(final PalletId palletId, final OperatorId operatorId) {
        final boolean alreadyCancelled = PalletStatus.CANCELLED.equals(getPallet(palletId, operatorId).getPalletStatus());
        final Pallet pallet = this.palletStorageService.cancel(palletId);
        return alreadyCancelled ? pallet : record(pallet, PalletHistoryEventType.CANCELLED, null);
    }

    @Override
    public List<PalletHistoryEntry> getHistory(final PalletId palletId, final OperatorId operatorId) {
        getPallet(palletId, operatorId);
        return this.palletHistoryRepository.findByPalletId(palletId);
    }

    @Override
    public void handlePalletLoaded(final PalletLoadedRequest palletLoadedRequest) {
        final PalletId palletId = palletLoadedRequest.palletId();
        try {
            final boolean changed = this.palletStorageService.markLoaded(palletId);
            if (changed) {
                record(this.palletStorageService.find(palletId), PalletHistoryEventType.LOADED, null);
                log.info("Pallet {} marked as loaded by event {}", palletId.value(), palletLoadedRequest.eventId());
            } else {
                log.info("Pallet {} already loaded, event {} ignored", palletId.value(), palletLoadedRequest.eventId());
            }
        } catch (final PalletNotFoundException | PalletModificationException
                       | PalletStatusTransitionException exception) {
            log.warn("Pallet loaded event {} for pallet {} cannot be applied: {}",
                    palletLoadedRequest.eventId(), palletId.value(), exception.getMessage());
        }
    }

    @Override
    public void determinePalletHandlingPriority(final PalletId palletId) {

    }

    @Override
    public void addSealNumber(final SealNumberRequest sealNumberRequest) {
        final PalletId palletId = sealNumberRequest.getPalletId();
        final SealNumber sealNumber = sealNumberRequest.getSealNumber();
        this.sealNumberService.validateSealNumber(palletId, sealNumber);
        this.palletStorageService.changeSealNumber(palletId, sealNumber);
    }

    @Override
    public void assignDriver(final AssignDriverRequest assignDriverRequest) {
        final PalletId palletId = assignDriverRequest.getPalletId();
        final DriverId driverId = assignDriverRequest.getDriverId();
        final Driver driver = this.driverStorageService.find(driverId);
        this.palletStorageService.changeDriver(palletId, driver);
    }

    @Override
    public void attachShipments(final ShipmentAttachRequest shipmentAttachRequest) {
        final Set<ShipmentId> shipmentIds = shipmentAttachRequest.getShipmentIds();
        final PalletId palletId = shipmentAttachRequest.getPalletId();
        for (final ShipmentId shipmentId : shipmentIds) {
            this.palletStorageService.addShipment(palletId, shipmentId);
        }
        record(this.palletStorageService.find(palletId), PalletHistoryEventType.SHIPMENTS_ADDED,
                shipmentIdsDetails(shipmentIds));
    }

    @Override
    public Pallet removeShipment(final PalletId palletId, final ShipmentId shipmentId, final OperatorId operatorId) {
        getPallet(palletId, operatorId);
        this.palletStorageService.removeShipment(palletId, shipmentId);
        return record(this.palletStorageService.find(palletId), PalletHistoryEventType.SHIPMENT_REMOVED,
                String.valueOf(shipmentId.value()));
    }

    @Override
    public Pallet changeGrossWeight(final PalletId palletId, final Weight grossWeight, final OperatorId operatorId) {
        final Pallet current = getPallet(palletId, operatorId);
        final boolean unchanged = sameWeight(current.getPalletWeight(), grossWeight);
        final Pallet pallet = this.palletStorageService.changeGrossWeight(palletId, grossWeight);
        return unchanged ? pallet : record(pallet, PalletHistoryEventType.GROSS_WEIGHT_CHANGED, weightDetails(grossWeight));
    }

    @Override
    public Pallet defineMaxGrossWeight(final PalletId palletId, final BigDecimal maxGrossWeight,
                                       final OperatorId operatorId) {
        getPallet(palletId, operatorId);
        return record(this.palletStorageService.defineMaxGrossWeight(palletId, maxGrossWeight),
                PalletHistoryEventType.MAX_WEIGHT_SET, maxGrossWeight.toPlainString() + " KG");
    }

    @Override
    public Pallet changeDepartments(final PalletId palletId, final DepartmentId originDepartmentId,
                                    final DepartmentId destinationDepartmentId, final OperatorId operatorId) {
        getPallet(palletId, operatorId);
        return record(this.palletStorageService.changeDepartments(palletId, originDepartmentId, destinationDepartmentId),
                PalletHistoryEventType.DEPARTMENTS_CHANGED,
                departmentDetails(originDepartmentId) + " -> " + departmentDetails(destinationDepartmentId));
    }

    @Override
    public Pallet updatePallet(final PalletId palletId, final PalletUpdate update, final OperatorId operatorId) {
        if (update == null) {
            throw new IllegalArgumentException("Pallet update is required");
        }
        getPallet(palletId, operatorId);
        final Pallet pallet = this.palletStorageService.update(palletId, update.palletType(),
                update.palletHandlingPriority(), update.refrigerated(), update.dimension(), update.sealNumber());
        return record(pallet, PalletHistoryEventType.PALLET_UPDATED, updateDetails(pallet));
    }

    @Override
    public Pallet changeStatus(final PalletId palletId, final PalletStatus targetStatus, final OperatorId operatorId) {
        final Pallet current = getPallet(palletId, operatorId);
        if (targetStatus != null && targetStatus.equals(current.getPalletStatus())) {
            return current;
        }
        final PalletStatus previousStatus = this.palletStorageService.changeTransportStatus(palletId, targetStatus);
        return record(this.palletStorageService.find(palletId), PalletHistoryEventType.STATUS_CHANGED,
                previousStatus + " -> " + targetStatus);
    }

    private String updateDetails(final Pallet pallet) {
        final StringBuilder details = new StringBuilder()
                .append(pallet.getPalletType())
                .append(" · ").append(pallet.getPalletHandlingPriority());
        if (Boolean.TRUE.equals(pallet.isRefrigerated())) {
            details.append(" · refrigerated");
        }
        if (pallet.getDimension() != null) {
            details.append(" · ").append(pallet.getDimension().getLength())
                    .append("x").append(pallet.getDimension().getWidth())
                    .append("x").append(pallet.getDimension().getHeight());
        }
        if (pallet.getSealNumber() != null && pallet.getSealNumber().value() != null) {
            details.append(" · seal ").append(pallet.getSealNumber().value());
        }
        return details.toString();
    }

    private Pallet record(final Pallet pallet, final PalletHistoryEventType eventType, final String details) {
        this.palletHistoryRepository.append(PalletHistoryEntry.of(pallet, eventType,
                this.currentUserServicePort.currentUser().orElse(null), details));
        return pallet;
    }

    private String shipmentIdsDetails(final Set<ShipmentId> shipmentIds) {
        return shipmentIds.stream()
                .map(shipmentId -> String.valueOf(shipmentId.value()))
                .collect(Collectors.joining(", "));
    }

    private String departmentDetails(final DepartmentId departmentId) {
        return departmentId == null ? "-" : String.valueOf(departmentId.value());
    }

    private String weightDetails(final Weight weight) {
        return weight == null || weight.getValue() == null
                ? "-"
                : weight.getValue().toPlainString() + " " + weight.getUnit();
    }

    private boolean sameWeight(final Weight current, final Weight next) {
        if (current == null || current.getValue() == null) {
            return next == null || next.getValue() == null;
        }
        if (next == null || next.getValue() == null) {
            return false;
        }
        return current.getUnit() == next.getUnit() && current.getValue().compareTo(next.getValue()) == 0;
    }

    private Pallet ensureOwnership(final Pallet pallet, final OperatorId operatorId) {
        if (!pallet.belongsTo(operatorId)) {
            throw new PalletNotFoundException("Pallet " + pallet.getPalletId().value() + " not found");
        }
        return pallet;
    }
}
