package com.warehouse.driverhub.domain.model;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.domain.enumeration.TransportType;
import com.warehouse.driverhub.domain.enumeration.VehicleType;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.vo.CarrierChange;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.domain.vo.VehicleChange;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carrier {

    private final CarrierId carrierId;
    private final OperatorId operatorId;
    private String name;
    private TaxId taxId;
    private String phone;
    private String email;
    private TransportType defaultTransportType;
    private boolean active;
    private final Instant created;
    private final List<CarrierDriver> drivers;
    private final List<CarrierVehicle> vehicles;

    public Carrier(final CarrierId carrierId, final OperatorId operatorId, final String name, final TaxId taxId,
                   final String phone, final String email, final TransportType defaultTransportType,
                   final boolean active, final Instant created, final List<CarrierDriver> drivers,
                   final List<CarrierVehicle> vehicles) {
        if (carrierId == null || carrierId.getValue() == null) {
            throw new IllegalArgumentException("Carrier id is required");
        }
        if (operatorId == null || operatorId.getValue() == null) {
            throw new IllegalArgumentException("Operator id is required");
        }
        this.carrierId = carrierId;
        this.operatorId = operatorId;
        this.name = requiredName(name);
        this.taxId = normalizeTaxId(taxId);
        this.phone = Text.trim(phone);
        this.email = Text.trim(email);
        this.defaultTransportType = defaultTransportType == null ? TransportType.EXTERNAL_CARRIER : defaultTransportType;
        this.active = active;
        this.created = created == null ? Instant.now() : created;
        this.drivers = drivers == null ? new ArrayList<>() : new ArrayList<>(drivers);
        this.vehicles = vehicles == null ? new ArrayList<>() : new ArrayList<>(vehicles);
    }

    public static Carrier register(final OperatorId operatorId, final String name, final TaxId taxId,
                                   final String phone, final String email, final TransportType defaultTransportType) {
        return new Carrier(CarrierId.generate(), operatorId, name, taxId, phone, email, defaultTransportType, true,
                Instant.now(), List.of(), List.of());
    }

    public void apply(final CarrierChange change) {
        switch (change) {
            case CarrierChange.Name nameChange -> this.name = requiredName(nameChange.value());
            case CarrierChange.Tax taxChange -> this.taxId = normalizeTaxId(taxChange.value());
            case CarrierChange.Phone phoneChange -> this.phone = Text.trim(phoneChange.value());
            case CarrierChange.Email emailChange -> this.email = Text.trim(emailChange.value());
            case CarrierChange.DefaultTransport transportChange -> {
                if (transportChange.value() == null) {
                    throw DriverHubException.invalid("Default transport type is required");
                }
                this.defaultTransportType = transportChange.value();
            }
            case CarrierChange.Active activeChange -> this.active = activeChange.value();
        }
    }

    public CarrierDriver addDriver(final String firstName, final String lastName, final String phone,
                                   final String licenseNumber) {
        final CarrierDriver driver = new CarrierDriver(DriverId.generate(), firstName, lastName, phone, licenseNumber,
                true);
        this.drivers.add(driver);
        return driver;
    }

    public void changeDriver(final DriverId driverId, final DriverChange change) {
        driver(driverId).apply(change);
    }

    public void addVehicle(final String registrationNumber, final VehicleType vehicleType,
                           final String trailerRegistrationNumber, final BigDecimal maxLoadKg) {
        final CarrierVehicle vehicle = new CarrierVehicle(VehicleId.generate(), registrationNumber, vehicleType,
                trailerRegistrationNumber, maxLoadKg, true);
        ensureUniqueRegistration(null, vehicle.getRegistrationNumber());
        this.vehicles.add(vehicle);
    }

    public void changeVehicle(final VehicleId vehicleId, final VehicleChange change) {
        if (change instanceof VehicleChange.RegistrationNumber registrationChange
                && !Text.blank(registrationChange.value())) {
            ensureUniqueRegistration(vehicleId, CarrierVehicle.normalizeRegistration(registrationChange.value()));
        }
        vehicle(vehicleId).apply(change);
    }

    public CarrierDriver driver(final DriverId driverId) {
        return this.drivers.stream()
                .filter(driver -> Objects.equals(driver.getDriverId(), driverId))
                .findFirst()
                .orElseThrow(() -> DriverHubException.notFound("Driver " + (driverId == null ? null : driverId.getValue())
                        + " not found"));
    }

    public CarrierVehicle vehicle(final VehicleId vehicleId) {
        return this.vehicles.stream()
                .filter(vehicle -> Objects.equals(vehicle.getVehicleId(), vehicleId))
                .findFirst()
                .orElseThrow(() -> DriverHubException.notFound("Vehicle " + (vehicleId == null ? null : vehicleId.value())
                        + " not found"));
    }

    private void ensureUniqueRegistration(final VehicleId vehicleId, final String registrationNumber) {
        final boolean duplicate = this.vehicles.stream()
                .filter(existing -> !Objects.equals(existing.getVehicleId(), vehicleId))
                .anyMatch(existing -> existing.getRegistrationNumber().equals(registrationNumber));
        if (duplicate) {
            throw DriverHubException.invalid("Vehicle " + registrationNumber + " already exists");
        }
    }

    private static String requiredName(final String value) {
        if (Text.blank(value)) {
            throw DriverHubException.invalid("Carrier name is required");
        }
        return value.trim();
    }

    private static TaxId normalizeTaxId(final TaxId taxId) {
        return taxId == null || Text.blank(taxId.getValue()) ? null : new TaxId(taxId.getValue().trim());
    }

    public CarrierId getCarrierId() {
        return carrierId;
    }

    public OperatorId getOperatorId() {
        return operatorId;
    }

    public String getName() {
        return name;
    }

    public TaxId getTaxId() {
        return taxId;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public TransportType getDefaultTransportType() {
        return defaultTransportType;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreated() {
        return created;
    }

    public List<CarrierDriver> getDrivers() {
        return List.copyOf(drivers);
    }

    public List<CarrierVehicle> getVehicles() {
        return List.copyOf(vehicles);
    }
}
