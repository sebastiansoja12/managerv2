package com.warehouse.palletmanagement.domain.service;

import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletCarrierAssignment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.TransportCarrier;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

public final class PalletTransportPolicy {

    private static final String PALLET_STATUS_NOT_MANUAL = "PALLET_STATUS_NOT_MANUAL";
    private static final String PALLET_CARRIER_REQUIRED = "PALLET_CARRIER_REQUIRED";
    private static final String PALLET_CARRIER_LOCKED = "PALLET_CARRIER_LOCKED";
    private static final String CARRIER_DRIVER_UNAVAILABLE = "CARRIER_DRIVER_UNAVAILABLE";
    private static final String CARRIER_VEHICLE_UNAVAILABLE = "CARRIER_VEHICLE_UNAVAILABLE";
    private static final String PALLET_VEHICLE_REQUIRED = "PALLET_VEHICLE_REQUIRED";

    private PalletTransportPolicy() {
    }

    public static void ensureCarrierChangeable(final PalletDetails pallet) {
        if (pallet.transportLocked()) {
            throw new PalletRequestRejectedException(409, PALLET_CARRIER_LOCKED,
                    "Carrier cannot be changed in status " + pallet.palletStatus());
        }
    }

    public static PalletCarrier assign(final TransportCarrier carrier,
                                       final PalletCarrierAssignment assignment,
                                       final Instant assignedAt) {
        final Optional<TransportCarrier.Driver> driver = Optional.ofNullable(assignment.driverId())
                .map(driverId -> carrier.driver(driverId).orElseThrow(() -> new PalletRequestRejectedException(409,
                        CARRIER_DRIVER_UNAVAILABLE, "Driver " + driverId.getValue() + " is not available")));
        final Optional<TransportCarrier.Vehicle> vehicle = Optional.ofNullable(assignment.vehicleId())
                .map(vehicleId -> carrier.vehicle(vehicleId).orElseThrow(() -> new PalletRequestRejectedException(409,
                        CARRIER_VEHICLE_UNAVAILABLE, "Vehicle " + vehicleId.value() + " is not available")));
        final String vehicleRegistration = firstText(assignment.vehicleRegistrationNumber(),
                vehicle.map(TransportCarrier.Vehicle::registrationNumber).orElse(null));
        if (vehicleRegistration == null) {
            throw new PalletRequestRejectedException(400, PALLET_VEHICLE_REQUIRED,
                    "Vehicle registration number is required");
        }
        return new PalletCarrier(
                carrier.carrierId(),
                carrier.name(),
                assignment.transportType() == null ? carrier.defaultTransportType() : assignment.transportType(),
                driver.map(TransportCarrier.Driver::driverId).orElse(null),
                firstText(assignment.driverName(), driver.map(TransportCarrier.Driver::fullName).orElse(null)),
                firstText(assignment.driverPhone(), driver.map(TransportCarrier.Driver::phone).orElse(null)),
                vehicle.map(TransportCarrier.Vehicle::vehicleId).orElse(null),
                upper(vehicleRegistration),
                upper(firstText(assignment.trailerRegistrationNumber(),
                        vehicle.map(TransportCarrier.Vehicle::trailerRegistrationNumber).orElse(null))),
                assignedAt,
                null);
    }

    public static void ensureManualStatusChange(final PalletStatus targetStatus, final boolean carrierAssigned) {
        if (targetStatus != PalletStatus.IN_TRANSIT && targetStatus != PalletStatus.DELIVERED) {
            throw new PalletRequestRejectedException(400, PALLET_STATUS_NOT_MANUAL,
                    "Pallet status " + targetStatus + " cannot be set manually");
        }
        if (targetStatus == PalletStatus.IN_TRANSIT && !carrierAssigned) {
            throw new PalletRequestRejectedException(409, PALLET_CARRIER_REQUIRED,
                    "Carrier must be assigned before the pallet departs");
        }
    }

    public static String describe(final PalletCarrier carrier) {
        final StringBuilder details = new StringBuilder(carrier.carrierName())
                .append(" · ").append(carrier.transportType())
                .append(" · ").append(carrier.vehicleRegistrationNumber());
        if (carrier.trailerRegistrationNumber() != null) {
            details.append(" / ").append(carrier.trailerRegistrationNumber());
        }
        if (carrier.driverName() != null) {
            details.append(" · ").append(carrier.driverName());
        }
        return details.toString();
    }

    private static String firstText(final String preferred, final String fallback) {
        if (preferred != null && !preferred.isBlank()) {
            return preferred.trim();
        }
        return fallback == null || fallback.isBlank() ? null : fallback.trim();
    }

    private static String upper(final String value) {
        return value == null ? null : value.toUpperCase(Locale.ROOT);
    }
}
