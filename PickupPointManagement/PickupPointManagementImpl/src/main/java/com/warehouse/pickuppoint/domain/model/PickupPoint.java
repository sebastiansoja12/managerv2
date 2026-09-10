package com.warehouse.pickuppoint.domain.model;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointStatus;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointStatusTransitionException;
import com.warehouse.pickuppoint.domain.exception.PickupPointUnavailableException;
import com.warehouse.pickuppoint.domain.vo.ExternalPointReference;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.OpeningSchedule;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;
import com.warehouse.pickuppoint.domain.vo.PickupPointContact;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;
import com.warehouse.pickuppoint.domain.vo.PickupPointServicePolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class PickupPoint {

    private final PickupPointId pickupPointId;
    private final PickupPointCode code;
    private final Instant createdAt;

    private String name;
    private PickupPointType type;
    private PickupPointStatus status;
    private Set<PickupPointCapability> capabilities;
    private PickupPointAddress address;
    private GeoCoordinates coordinates;
    private DepartmentId departmentId;
    private PickupPointContact contact;
    private String accessInstructions;
    private OpeningSchedule openingSchedule;
    private PickupPointServicePolicy servicePolicy;
    private ExternalPointReference externalReference;
    private String statusReason;
    private long version;
    private Instant updatedAt;

    private PickupPoint(final PickupPointSnapshot snapshot) {
        this.pickupPointId = requiredId(snapshot.pickupPointId());
        this.code = Objects.requireNonNull(snapshot.code(), "Pickup point code cannot be null");
        this.name = validName(snapshot.name());
        this.type = Objects.requireNonNull(snapshot.type(), "Pickup point type cannot be null");
        this.status = Objects.requireNonNull(snapshot.status(), "Pickup point status cannot be null");
        this.capabilities = validCapabilities(snapshot.capabilities());
        this.address = snapshot.address();
        this.coordinates = snapshot.coordinates();
        this.departmentId = validDepartmentId(snapshot.departmentId());
        this.contact = snapshot.contact();
        this.accessInstructions = validInstructions(snapshot.accessInstructions());
        this.openingSchedule = snapshot.openingSchedule();
        this.servicePolicy = snapshot.servicePolicy();
        this.externalReference = snapshot.externalReference();
        this.statusReason = snapshot.statusReason();
        this.version = validVersion(snapshot.version());
        this.createdAt = Objects.requireNonNull(snapshot.createdAt(), "Creation timestamp cannot be null");
        this.updatedAt = Objects.requireNonNull(snapshot.updatedAt(), "Update timestamp cannot be null");
        if (this.status == PickupPointStatus.ACTIVE) {
            validateActivationConfiguration(true);
        }
    }

    public static PickupPoint create(
            final PickupPointId pickupPointId,
            final PickupPointCode code,
            final String name,
            final PickupPointType type,
            final Set<PickupPointCapability> capabilities,
            final PickupPointAddress address,
            final GeoCoordinates coordinates,
            final DepartmentId departmentId,
            final PickupPointContact contact,
            final String accessInstructions,
            final OpeningSchedule openingSchedule,
            final PickupPointServicePolicy servicePolicy,
            final ExternalPointReference externalReference,
            final Instant timestamp) {
        final Instant requiredTimestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        return new PickupPoint(new PickupPointSnapshot(
                pickupPointId,
                code,
                name,
                type,
                PickupPointStatus.ACTIVE,
                capabilities,
                address,
                coordinates,
                departmentId,
                contact,
                accessInstructions,
                openingSchedule,
                servicePolicy,
                externalReference,
                null,
                0,
                requiredTimestamp,
                requiredTimestamp));
    }

    public static PickupPoint restore(final PickupPointSnapshot snapshot) {
        return new PickupPoint(Objects.requireNonNull(snapshot, "Pickup point snapshot cannot be null"));
    }

    public boolean updateDetails(
            final String name,
            final PickupPointAddress address,
            final GeoCoordinates coordinates,
            final DepartmentId departmentId,
            final PickupPointContact contact,
            final String accessInstructions,
            final ExternalPointReference externalReference,
            final Instant timestamp) {
        ensureMutable();
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        final String validName = validName(name);
        final DepartmentId validDepartmentId = validDepartmentId(departmentId);
        final String validInstructions = validInstructions(accessInstructions);
        if (address == null || coordinates == null || validDepartmentId == null) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point must have an address, coordinates and assigned department");
        }
        if (Objects.equals(this.name, validName)
                && Objects.equals(this.address, address)
                && Objects.equals(this.coordinates, coordinates)
                && Objects.equals(this.departmentId, validDepartmentId)
                && Objects.equals(this.contact, contact)
                && Objects.equals(this.accessInstructions, validInstructions)
                && Objects.equals(this.externalReference, externalReference)) {
            return false;
        }
        this.name = validName;
        this.address = address;
        this.coordinates = coordinates;
        this.departmentId = validDepartmentId;
        this.contact = contact;
        this.accessInstructions = validInstructions;
        this.externalReference = externalReference;
        touch(requiredTimestamp);
        return true;
    }

    public boolean changeType(final PickupPointType type, final Instant timestamp) {
        ensureMutable();
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        final PickupPointType requiredType = Objects.requireNonNull(type, "Pickup point type cannot be null");
        if (this.type == requiredType) {
            return false;
        }
        this.type = requiredType;
        touch(requiredTimestamp);
        return true;
    }

    public boolean changeCapabilities(final Set<PickupPointCapability> capabilities, final Instant timestamp) {
        ensureMutable();
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        final Set<PickupPointCapability> validCapabilities = validCapabilities(capabilities);
        if (this.capabilities.equals(validCapabilities)) {
            return false;
        }
        this.capabilities = validCapabilities;
        touch(requiredTimestamp);
        return true;
    }

    public boolean changeOpeningSchedule(final OpeningSchedule openingSchedule, final Instant timestamp) {
        ensureMutable();
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        if (openingSchedule == null) {
            throw new InvalidPickupPointConfigurationException("Pickup point must have an opening schedule");
        }
        if (Objects.equals(this.openingSchedule, openingSchedule)) {
            return false;
        }
        this.openingSchedule = openingSchedule;
        touch(requiredTimestamp);
        return true;
    }

    public boolean changeServicePolicy(final PickupPointServicePolicy servicePolicy, final Instant timestamp) {
        ensureMutable();
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        if (servicePolicy == null) {
            throw new InvalidPickupPointConfigurationException("Pickup point must have a service policy");
        }
        if (Objects.equals(this.servicePolicy, servicePolicy)) {
            return false;
        }
        this.servicePolicy = servicePolicy;
        touch(requiredTimestamp);
        return true;
    }

    public boolean suspend(final String reason, final Instant timestamp) {
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        if (this.status == PickupPointStatus.SUSPENDED) {
            return false;
        }
        if (this.status != PickupPointStatus.ACTIVE) {
            throw new InvalidPickupPointStatusTransitionException(this.status, PickupPointStatus.SUSPENDED);
        }
        this.status = PickupPointStatus.SUSPENDED;
        this.statusReason = validReason(reason);
        touch(requiredTimestamp);
        return true;
    }

    public boolean resume(final boolean departmentActive, final Instant timestamp) {
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        if (this.status == PickupPointStatus.ACTIVE) {
            return false;
        }
        if (this.status != PickupPointStatus.SUSPENDED) {
            throw new InvalidPickupPointStatusTransitionException(this.status, PickupPointStatus.ACTIVE);
        }
        validateActivationConfiguration(departmentActive);
        this.status = PickupPointStatus.ACTIVE;
        this.statusReason = null;
        touch(requiredTimestamp);
        return true;
    }

    public boolean close(final String reason, final Instant timestamp) {
        final Instant requiredTimestamp = requiredTimestamp(timestamp);
        if (this.status == PickupPointStatus.CLOSED) {
            return false;
        }
        this.status = PickupPointStatus.CLOSED;
        this.statusReason = validReason(reason);
        touch(requiredTimestamp);
        return true;
    }

    public void validateSelection(final PickupPointSelectionCriteria criteria, final boolean departmentActive) {
        Objects.requireNonNull(criteria, "Pickup point selection criteria cannot be null");
        if (this.status != PickupPointStatus.ACTIVE) {
            throw new PickupPointUnavailableException("Pickup point is not active");
        }
        if (!departmentActive) {
            throw new PickupPointUnavailableException("Pickup point department is not active");
        }
        if (this.type != criteria.requiredType()) {
            throw new PickupPointUnavailableException("Pickup point type does not match the selected shipment method");
        }
        if (!this.capabilities.contains(criteria.requiredCapability())) {
            throw new PickupPointUnavailableException("Pickup point does not support the required service");
        }
        if (this.address == null || this.address.countryCode() != criteria.countryCode()) {
            throw new PickupPointUnavailableException("Pickup point country does not match the shipment country");
        }
        if (this.servicePolicy == null
                || !this.servicePolicy.accepts(criteria.shipmentSize(), criteria.dangerousGoods())) {
            throw new PickupPointUnavailableException("Pickup point does not accept this shipment");
        }
    }

    public boolean isOpenAt(final Instant timestamp) {
        return this.openingSchedule != null && this.openingSchedule.isOpenAt(timestamp);
    }

    public PickupPointSnapshot snapshot() {
        return new PickupPointSnapshot(
                this.pickupPointId, this.code, this.name, this.type, this.status, this.capabilities,
                this.address, this.coordinates, this.departmentId, this.contact, this.accessInstructions,
                this.openingSchedule, this.servicePolicy, this.externalReference, this.statusReason,
                this.version, this.createdAt, this.updatedAt);
    }

    private void validateActivationConfiguration(final boolean departmentActive) {
        if (this.address == null || this.coordinates == null || this.departmentId == null
                || this.openingSchedule == null || this.servicePolicy == null) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point must have an address, coordinates, department, opening schedule and service policy");
        }
        if (!departmentActive) {
            throw new InvalidPickupPointConfigurationException("Pickup point department must be active");
        }
    }

    private void ensureMutable() {
        if (this.status == PickupPointStatus.CLOSED) {
            throw new InvalidPickupPointConfigurationException("Closed pickup point cannot be changed");
        }
    }

    private void touch(final Instant timestamp) {
        this.updatedAt = timestamp;
    }

    private static Instant requiredTimestamp(final Instant timestamp) {
        return Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    private static PickupPointId requiredId(final PickupPointId pickupPointId) {
        Objects.requireNonNull(pickupPointId, "Pickup point ID cannot be null");
        Objects.requireNonNull(pickupPointId.value(), "Pickup point ID value cannot be null");
        return pickupPointId;
    }

    private static DepartmentId validDepartmentId(final DepartmentId departmentId) {
        if (departmentId != null && departmentId.value() == null) {
            throw new InvalidPickupPointConfigurationException("Department ID value cannot be null");
        }
        return departmentId;
    }

    private static Set<PickupPointCapability> validCapabilities(final Set<PickupPointCapability> capabilities) {
        final Set<PickupPointCapability> copiedCapabilities = Set.copyOf(
                Objects.requireNonNull(capabilities, "Pickup point capabilities cannot be null"));
        if (copiedCapabilities.isEmpty()) {
            throw new InvalidPickupPointConfigurationException("Pickup point must support at least one capability");
        }
        return copiedCapabilities;
    }

    private static String validName(final String name) {
        if (name == null || name.isBlank() || name.trim().length() > 160) {
            throw new InvalidPickupPointConfigurationException("Pickup point name must contain 1 to 160 characters");
        }
        return name.trim();
    }

    private static String validInstructions(final String instructions) {
        if (instructions == null || instructions.isBlank()) {
            return null;
        }
        final String normalized = instructions.trim();
        if (normalized.length() > 1000) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point access instructions cannot exceed 1000 characters");
        }
        return normalized;
    }

    private static String validReason(final String reason) {
        if (reason == null || reason.isBlank() || reason.trim().length() > 500) {
            throw new InvalidPickupPointConfigurationException(
                    "Pickup point status reason must contain 1 to 500 characters");
        }
        return reason.trim();
    }

    private static long validVersion(final long version) {
        if (version < 0) {
            throw new InvalidPickupPointConfigurationException("Pickup point version cannot be negative");
        }
        return version;
    }
}
