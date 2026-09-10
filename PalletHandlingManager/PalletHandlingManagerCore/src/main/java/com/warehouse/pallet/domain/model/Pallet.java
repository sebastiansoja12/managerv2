package com.warehouse.pallet.domain.model;

import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.*;
import com.warehouse.pallet.domain.exception.MaxPalletWeightAlreadySetException;
import com.warehouse.pallet.domain.exception.PalletLockedException;
import com.warehouse.pallet.domain.exception.PalletModificationException;
import com.warehouse.pallet.domain.exception.PalletStatusTransitionException;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.domain.vo.MaxPalletWeight;
import com.warehouse.pallet.configuration.identificator.OperatorId;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import com.warehouse.pallet.infrastructure.adapter.secondary.document.PalletDocument;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Pallet {
    private PalletId palletId;
    private TrackingNumber trackingNumber;
    private OperatorId operatorId;
    private Set<ShipmentId> shipmentIds;
    private DepartmentId originDepartmentId;
    private DepartmentId destinationDepartmentId;
    private Instant created;
    private Instant modified;
    private PalletStatus palletStatus;
    private StorageStatus storageStatus;
    private Driver driver;
    private Weight palletWeight;
    private Dimension dimension;
    private PalletHandlingPriority palletHandlingPriority;
    private PalletType palletType;
    private SealNumber sealNumber;
    private Boolean refrigerated;
    private MaxPalletWeight maxPalletWeight;

	public Pallet(final PalletId palletId,
                  final TrackingNumber trackingNumber,
                  final OperatorId operatorId,
                  final Set<ShipmentId> shipmentIds,
                  final DepartmentId originDepartmentId,
                  final DepartmentId destinationDepartmentId,
                  final PalletStatus palletStatus,
                  final StorageStatus storageStatus,
                  final Driver driver,
                  final Weight palletWeight,
                  final Dimension dimension,
                  final PalletHandlingPriority palletHandlingPriority,
                  final PalletType palletType,
                  final SealNumber sealNumber,
                  final Boolean refrigerated,
                  final MaxPalletWeight maxPalletWeight) {
        Assert.notNull(trackingNumber, "Tracking Number cannot be null");
        Assert.notNull(operatorId, "Operator Id cannot be null");
        this.palletId = palletId;
        this.trackingNumber = trackingNumber;
        this.operatorId = operatorId;
        this.shipmentIds = shipmentIds;
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.created = Instant.now();
        this.modified = Instant.now();
        this.palletStatus = palletStatus;
        this.storageStatus = storageStatus;
        this.driver = driver;
        this.palletWeight = palletWeight;
        this.dimension = dimension;
        this.palletHandlingPriority = palletHandlingPriority;
        this.palletType = palletType;
        this.sealNumber = sealNumber;
        this.refrigerated = refrigerated;
        this.maxPalletWeight = maxPalletWeight;
    }

    public Pallet(final PalletId palletId,
                  final TrackingNumber trackingNumber,
                  final OperatorId operatorId,
                  final Set<ShipmentId> shipmentIds,
                  final DepartmentId originDepartmentId,
                  final DepartmentId destinationDepartmentId,
                  final Instant created,
                  final Instant modified,
                  final PalletStatus palletStatus,
                  final StorageStatus storageStatus,
                  final Driver driver,
                  final Weight palletWeight,
                  final Dimension dimension,
                  final PalletHandlingPriority palletHandlingPriority,
                  final PalletType palletType,
                  final SealNumber sealNumber,
                  final Boolean refrigerated,
                  final MaxPalletWeight maxPalletWeight) {
        Assert.notNull(palletId, "Pallet Id cannot be null");
        Assert.notNull(shipmentIds, "Shipment Ids cannot be null");
        Assert.notNull(created, "Created cannot be null");
        Assert.notNull(modified, "Modified cannot be null");
        Assert.notNull(palletStatus, "Pallet Status cannot be null");
        Assert.notNull(storageStatus, "Storage Status cannot be null");
        Assert.notNull(driver, "Driver cannot be null");
        Assert.notNull(palletHandlingPriority, "Pallet Handling Priority cannot be null");
        Assert.notNull(palletType, "Pallet Type cannot be null");
        Assert.notNull(refrigerated, "Refrigerated cannot be null");
        this.palletId = palletId;
        this.trackingNumber = trackingNumber;
        this.operatorId = operatorId;
        this.shipmentIds = shipmentIds;
        this.originDepartmentId = originDepartmentId;
        this.destinationDepartmentId = destinationDepartmentId;
        this.created = created;
        this.modified = modified;
        this.palletStatus = palletStatus;
        this.storageStatus = storageStatus;
        this.driver = driver;
        this.palletWeight = palletWeight;
        this.dimension = dimension;
        this.palletHandlingPriority = palletHandlingPriority;
        this.palletType = palletType;
        this.sealNumber = sealNumber;
        this.refrigerated = refrigerated;
        this.maxPalletWeight = maxPalletWeight;
    }

    private Pallet(final PalletId palletId, final TrackingNumber trackingNumber, final OperatorId operatorId) {
        Assert.notNull(palletId, "pallet id must not be null");
        Assert.notNull(trackingNumber, "tracking number must not be null");
        Assert.notNull(operatorId, "operator id must not be null");
        this.palletId = palletId;
        this.trackingNumber = trackingNumber;
        this.operatorId = operatorId;
        this.shipmentIds = new HashSet<>();
        this.originDepartmentId = null;
        this.destinationDepartmentId = null;
        this.created = Instant.now();
        this.modified = Instant.now();
        this.palletStatus = PalletStatus.CREATED;
        this.storageStatus = StorageStatus.UNLOCKED;
        this.driver = Driver.empty();
        this.palletWeight = null;
        this.dimension = null;
        this.palletHandlingPriority = PalletHandlingPriority.LOW;
        this.sealNumber = null;
        this.refrigerated = false;
        this.maxPalletWeight = null;
        this.palletType = PalletType.PARENT;
    }

    public static Pallet from(final PalletDocument pallet) {
        final Set<ShipmentId> shipmentIds = pallet.getShipmentIds() == null
                ? new HashSet<>()
                : new HashSet<>(pallet.getShipmentIds());
		final Pallet result = new Pallet(pallet.getPalletId(), pallet.getTrackingNumber(), pallet.getOperatorId(),
                shipmentIds, pallet.getOriginDepartmentId(), pallet.getDestinationDepartmentId(), pallet.getCreated(),
                pallet.getModified(), pallet.getPalletStatus(), pallet.getStorageStatus(),
                pallet.getDriver() == null ? Driver.empty() : Driver.from(pallet.getDriver()),
                pallet.getPalletWeight(), pallet.getDimension(), pallet.getPalletHandlingPriority(),
				PalletType.valueOf(pallet.getPalletType().name()), pallet.getSealNumber(),
                pallet.isRefrigerated() != null && pallet.isRefrigerated(), pallet.getMaxPalletWeight());
        result.resolveLegacyStatus();
        return result;
    }

    public static Pallet empty(final PalletId palletId, final TrackingNumber trackingNumber,
                               final OperatorId operatorId) {
        return new Pallet(palletId, trackingNumber, operatorId);
    }

    public static Pallet withShipments(final PalletId palletId,
                                       final TrackingNumber trackingNumber,
                                       final OperatorId operatorId,
                                       final Collection<ShipmentId> shipmentIds) {
        if (shipmentIds == null || shipmentIds.isEmpty()) {
            throw new PalletModificationException("Pallet must contain at least one shipment");
        }
        final Pallet pallet = new Pallet(palletId, trackingNumber, operatorId);
        pallet.shipmentIds.addAll(shipmentIds);
        pallet.evaluateReadiness();
        return pallet;
    }

    public DepartmentId getDestinationDepartmentId() {
        return destinationDepartmentId;
    }

    public PalletId getPalletId() {
        return palletId;
    }

    public TrackingNumber getTrackingNumber() {
        return trackingNumber;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public Set<ShipmentId> getShipmentIds() {
        if (this.shipmentIds == null) {
            this.shipmentIds = new HashSet<>();
        }
        return this.shipmentIds;
    }

    public DepartmentId getOriginDepartmentId() {
        return originDepartmentId;
    }

    public Instant getCreated() {
        return created;
    }

    public Instant getModified() {
        return modified;
    }

    public PalletStatus getPalletStatus() {
        return palletStatus;
    }

    public StorageStatus getStorageStatus() {
        return storageStatus;
    }

    public Driver getDriver() {
        return driver;
    }

    public Weight getPalletWeight() {
        return palletWeight;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public PalletHandlingPriority getPalletHandlingPriority() {
        return palletHandlingPriority;
    }

    public SealNumber getSealNumber() {
        return sealNumber;
    }

    public Boolean isRefrigerated() {
        return refrigerated;
    }

    public MaxPalletWeight getMaxPalletWeight() {
        return maxPalletWeight;
    }

    public PalletType getPalletType() {
        return palletType;
    }

    public boolean isEmpty() {
        return getShipmentIds().isEmpty();
    }

    public boolean isLocked() {
        return StorageStatus.LOCKED.equals(this.storageStatus);
    }

    public boolean belongsTo(final OperatorId operatorId) {
        return this.operatorId == null || this.operatorId.equals(operatorId);
    }

    public boolean isOverweight() {
        if (this.palletWeight == null || this.palletWeight.getValue() == null || this.palletWeight.getUnit() == null
                || this.maxPalletWeight == null || this.maxPalletWeight.getValue() == null
                || this.maxPalletWeight.getUnit() == null) {
            return false;
        }
        final BigDecimal grossWeight = this.palletWeight.getUnit().toKilograms(this.palletWeight.getValue());
        final BigDecimal maxGrossWeight = this.maxPalletWeight.getUnit().toKilograms(this.maxPalletWeight.getValue());
        return grossWeight.compareTo(maxGrossWeight) > 0;
    }

    public Optional<PalletLoadBlockReason> loadBlockReason() {
        if (PalletStatus.LOADED.equals(this.palletStatus)) {
            return Optional.empty();
        }
        if (isLocked()) {
            return Optional.of(PalletLoadBlockReason.LOCKED);
        }
        if (isEmpty()) {
            return Optional.of(PalletLoadBlockReason.EMPTY);
        }
        if (isOverweight()) {
            return Optional.of(PalletLoadBlockReason.OVERWEIGHT);
        }
        if (!PalletStatus.READY.equals(this.palletStatus)) {
            return Optional.of(PalletLoadBlockReason.INVALID_STATUS);
        }
        return Optional.empty();
    }

    public boolean canBeLoaded() {
        return loadBlockReason().isEmpty();
    }

    public boolean markLoaded() {
        if (PalletStatus.LOADED.equals(this.palletStatus)) {
            return false;
        }
        final Optional<PalletLoadBlockReason> blockReason = loadBlockReason();
        if (blockReason.isPresent()) {
            throw new PalletModificationException("Pallet cannot be loaded: " + blockReason.get());
        }
        changePalletStatus(PalletStatus.LOADED);
        return true;
    }

    public boolean cancel() {
        if (PalletStatus.CANCELLED.equals(this.palletStatus)) {
            return false;
        }
        changePalletStatus(PalletStatus.CANCELLED);
        return true;
    }

    public void changeOriginDepartmentId(final DepartmentId originDepartmentId) {
        this.originDepartmentId = originDepartmentId;
        modified();
    }

    public void changeDestinationDepartmentId(final DepartmentId destinationDepartmentId) {
        this.destinationDepartmentId = destinationDepartmentId;
        modified();
    }

    public void addShipment(final ShipmentId shipmentId) {
        ensureContentModifiable();
        this.getShipmentIds().add(shipmentId);
        evaluateReadiness();
        modified();
    }

    public void removeShipment(final ShipmentId shipmentId) {
        ensureContentModifiable();
        this.getShipmentIds().remove(shipmentId);
        evaluateReadiness();
        modified();
    }

    private void ensureContentModifiable() {
        if (isLocked()) {
            throw new PalletLockedException("Pallet is locked");
        }
        if (!this.palletStatus.isContentModifiable()) {
            throw new PalletModificationException("Pallet content cannot be changed in status " + this.palletStatus);
        }
    }

    public void changePalletStatus(final PalletStatus palletStatus) {
        Assert.notNull(palletStatus, "Pallet Status cannot be null");
        if (!this.palletStatus.canTransitionTo(palletStatus)) {
            throw new PalletStatusTransitionException(
                    "Pallet status cannot be changed from " + this.palletStatus + " to " + palletStatus);
        }
        this.palletStatus = palletStatus;
        modified();
    }

    public void update(final PalletType palletType,
                       final PalletHandlingPriority palletHandlingPriority,
                       final Boolean refrigerated,
                       final Dimension dimension,
                       final SealNumber sealNumber) {
        if (this.palletStatus.isFinished()) {
            throw new PalletModificationException("Pallet cannot be changed in status " + this.palletStatus);
        }
        if (dimension != null && (dimension.getLength() <= 0 || dimension.getWidth() <= 0 || dimension.getHeight() <= 0)) {
            throw new PalletModificationException("Pallet dimensions must be positive");
        }
        if (palletType != null) {
            this.palletType = palletType;
        }
        if (palletHandlingPriority != null) {
            this.palletHandlingPriority = palletHandlingPriority;
        }
        if (refrigerated != null) {
            this.refrigerated = refrigerated;
        }
        this.dimension = dimension;
        this.sealNumber = sealNumber == null || sealNumber.value() == null || sealNumber.value().isBlank()
                ? null
                : new SealNumber(sealNumber.value().trim());
        modified();
    }

    public PalletStatus changeTransportStatus(final PalletStatus targetStatus) {
        Assert.notNull(targetStatus, "Pallet Status cannot be null");
        if (!targetStatus.isManuallySettable()) {
            throw new PalletStatusTransitionException("Pallet status " + targetStatus + " cannot be set manually");
        }
        final PalletStatus previousStatus = this.palletStatus;
        changePalletStatus(targetStatus);
        return previousStatus;
    }

    public void changeStorageStatus(final StorageStatus storageStatus) {
        this.storageStatus = storageStatus;
        modified();
    }

    public void changeDriver(final Driver driver) {
        this.driver = driver;
        modified();
    }

    public void changeDimension(final Dimension dimension) {
        this.dimension = dimension;
        modified();
    }

    public void changePalletHandlingPriority(final PalletHandlingPriority palletHandlingPriority) {
        this.palletHandlingPriority = palletHandlingPriority;
        modified();
    }

    public void changeSealNumber(final SealNumber sealNumber) {
        this.sealNumber = sealNumber;
        modified();
    }

    public void changeRefrigerated(final Boolean refrigerated) {
        this.refrigerated = refrigerated;
        modified();
    }

    public void changeMaxPalletWeight(final BigDecimal maxPalletWeight) {
        if (maxPalletWeight == null || maxPalletWeight.signum() <= 0) {
            throw new PalletModificationException("Max pallet weight must be positive");
        }
        if (this.maxPalletWeight != null && this.maxPalletWeight.getValue() != null) {
            throw new MaxPalletWeightAlreadySetException("Max pallet weight is already set and cannot be changed");
        }
        this.maxPalletWeight = new MaxPalletWeight(maxPalletWeight, Unit.KG);
        reevaluateReadinessIfModifiable();
        modified();
    }

    public void changeWeight(final Weight palletWeight) {
        if (palletWeight != null && palletWeight.getValue() != null && palletWeight.getValue().signum() < 0) {
            throw new PalletModificationException("Pallet weight cannot be negative");
        }
        this.palletWeight = palletWeight;
        reevaluateReadinessIfModifiable();
        modified();
    }

    private void reevaluateReadinessIfModifiable() {
        if (this.palletStatus.isContentModifiable() && !PalletStatus.CREATED.equals(this.palletStatus)) {
            evaluateReadiness();
        }
    }

    private void evaluateReadiness() {
        final PalletStatus targetStatus;
        if (isEmpty()) {
            targetStatus = PalletStatus.PREPARING;
        } else if (isOverweight()) {
            targetStatus = PalletStatus.NOT_READY;
        } else {
            targetStatus = PalletStatus.READY;
        }
        changePalletStatus(targetStatus);
    }

    private void resolveLegacyStatus() {
        if (PalletStatus.EMPTY.equals(this.palletStatus)) {
            this.palletStatus = PalletStatus.CREATED;
            if (!isEmpty()) {
                evaluateReadiness();
            }
        }
    }

    private void modified() {
        this.modified = Instant.now();
    }
}
