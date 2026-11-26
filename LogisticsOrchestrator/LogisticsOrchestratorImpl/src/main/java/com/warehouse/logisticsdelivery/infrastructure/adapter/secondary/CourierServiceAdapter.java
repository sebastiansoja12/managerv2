package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.application.port.secondary.CourierServicePort;
import com.warehouse.logisticsdelivery.domain.vo.AssignedCourier;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import com.warehouse.supplier.api.dto.DeliveryAreaCourierDto;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourierServiceAdapter implements CourierServicePort {

    private final SupplierApiService supplierApiService;

    private final SupplierConfigurationApiService supplierConfigurationApiService;

    public CourierServiceAdapter(final SupplierApiService supplierApiService,
                                 final SupplierConfigurationApiService supplierConfigurationApiService) {
        this.supplierApiService = supplierApiService;
        this.supplierConfigurationApiService = supplierConfigurationApiService;
    }

    @Override
    public List<Courier> findCouriers(final DepartmentId departmentId) {
        return this.supplierApiService.findDeliveryAreaCouriers(departmentId).stream()
                .map(courier -> toCourier(courier, this.supplierConfigurationApiService
                        .findBySupplierId(courier.supplierId())
                        .map(SupplierConfigurationDto::maxShipmentsPerRoute)
                        .orElse(null)))
                .toList();
    }

    @Override
    public Optional<AssignedCourier> findCourier(final SupplierId supplierId) {
        return this.supplierApiService.findProfileById(supplierId)
                .map(profile -> new AssignedCourier(profile.supplierCode(),
                        (profile.firstName() + " " + profile.lastName()).trim()));
    }

    private Courier toCourier(final DeliveryAreaCourierDto courier, final Integer maxShipmentsPerRoute) {
        return new Courier(courier.supplierId(), courier.vehicleId(),
                courier.postalCodes().stream()
                        .map(String::trim)
                        .collect(Collectors.toUnmodifiableSet()),
                maxShipmentsPerRoute);
    }
}
