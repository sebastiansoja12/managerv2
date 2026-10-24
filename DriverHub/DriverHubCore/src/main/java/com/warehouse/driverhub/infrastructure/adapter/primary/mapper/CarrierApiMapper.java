package com.warehouse.driverhub.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.driverhub.api.dto.CarrierDriverDto;
import com.warehouse.driverhub.api.dto.CarrierDto;
import com.warehouse.driverhub.api.dto.CarrierVehicleDto;
import com.warehouse.driverhub.application.port.primary.command.CarrierCommand;
import com.warehouse.driverhub.application.port.primary.command.DriverCommand;
import com.warehouse.driverhub.application.port.primary.command.VehicleCommand;
import com.warehouse.driverhub.domain.enumeration.TransportType;
import com.warehouse.driverhub.domain.enumeration.VehicleType;
import com.warehouse.driverhub.domain.exception.DriverHubException;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.vo.DriverDetails;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.CarrierRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.CarrierVehicleRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.DriverRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.DriverResponseApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.FieldValueRequestApi;

import java.math.BigDecimal;

public final class CarrierApiMapper {

    private CarrierApiMapper() {
    }

    public static CarrierDto map(final Carrier carrier) {
        return new CarrierDto(carrier.getCarrierId().getValue(), carrier.getName(),
                carrier.getTaxId() == null ? null : carrier.getTaxId().getValue(), carrier.getPhone(),
                carrier.getEmail(), carrier.getDefaultTransportType().name(), carrier.isActive(), carrier.getCreated(),
                carrier.getDrivers().stream()
                        .map(driver -> new CarrierDriverDto(driver.getDriverId().getValue(), driver.getFirstName(),
                                driver.getLastName(), driver.getPhone(), driver.getLicenseNumber(), driver.isActive()))
                        .toList(),
                carrier.getVehicles().stream()
                        .map(vehicle -> new CarrierVehicleDto(vehicle.getVehicleId().value(), vehicle.getRegistrationNumber(),
                                vehicle.getBrand(), vehicle.getModel(),
                                vehicle.getVehicleType().name(), vehicle.getTrailerRegistrationNumber(),
                                vehicle.getMaxLoadKg(), vehicle.isActive()))
                        .toList());
    }

    public static DriverResponseApi map(final DriverDetails details) {
        return new DriverResponseApi(details.driver().getDriverId().getValue(), details.carrierId().getValue(),
                details.carrierName(), details.carrierActive(), details.driver().getFirstName(),
                details.driver().getLastName(), details.driver().getPhone(), details.driver().getLicenseNumber(),
                details.driver().isActive());
    }

    public static CarrierCommand map(final CarrierRequestApi request) {
        requireBody(request);
        return new CarrierCommand(request.name(), taxId(request.taxId()), request.phone(), request.email(),
                transportType(request.defaultTransportType()));
    }

    public static DriverCommand map(final DriverRequestApi request) {
        requireBody(request);
        return new DriverCommand(request.firstName(), request.lastName(), request.phone(), request.licenseNumber());
    }

    public static VehicleCommand map(final CarrierVehicleRequestApi request) {
        requireBody(request);
        return new VehicleCommand(request.registrationNumber(), request.brand(), request.model(),
                vehicleType(request.vehicleType()),
                request.trailerRegistrationNumber(), request.maxLoadKg());
    }

    public static String value(final FieldValueRequestApi request) {
        requireBody(request);
        return request.value();
    }

    public static TaxId taxId(final String value) {
        return value == null || value.isBlank() ? null : TaxId.of(value.trim());
    }

    public static TransportType transportType(final String value) {
        return enumValue(TransportType.class, value);
    }

    public static VehicleType vehicleType(final String value) {
        return enumValue(VehicleType.class, value);
    }

    public static BigDecimal decimal(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim().replace(',', '.'));
        } catch (final NumberFormatException exception) {
            throw DriverHubException.invalid("Invalid number " + value);
        }
    }

    private static void requireBody(final Object request) {
        if (request == null) {
            throw DriverHubException.invalid("Request body is required");
        }
    }

    private static <E extends Enum<E>> E enumValue(final Class<E> type, final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.trim());
        } catch (final IllegalArgumentException exception) {
            throw DriverHubException.invalid("Unknown value " + value);
        }
    }
}
