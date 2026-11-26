package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import com.warehouse.supplier.api.dto.DeliveryAreaCourierDto;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;

class CourierServiceAdapterTest {

    private final SupplierApiService supplierApiService = mock(SupplierApiService.class);

    private final SupplierConfigurationApiService supplierConfigurationApiService =
            mock(SupplierConfigurationApiService.class);

    private final CourierServiceAdapter adapter = new CourierServiceAdapter(supplierApiService,
            supplierConfigurationApiService);

    @Test
    void shouldIncludeDeliveryAreaCourierRegardlessOfAutoAssignSetting() {
        final DepartmentId departmentId = new DepartmentId(1L);
        final SupplierId supplierId = new SupplierId(10L);
        when(supplierApiService.findDeliveryAreaCouriers(departmentId)).thenReturn(List.of(
                new DeliveryAreaCourierDto(supplierId, new VehicleId(20L), Set.of(" 44-204 "))));
        when(supplierConfigurationApiService.findBySupplierId(supplierId)).thenReturn(Optional.of(
                new SupplierConfigurationDto(supplierId, Map.of("AUTO_ASSIGN_SHIPMENTS_BY_AREA", false), 3, 15,
                        null, "PDF_A6", "DELIVERY_ORDER")));

        final List<Courier> couriers = adapter.findCouriers(departmentId);

        assertEquals(List.of(new Courier(supplierId, new VehicleId(20L), Set.of("44-204"), 15)), couriers);
    }

    @Test
    void shouldIncludeDeliveryAreaCourierWithoutConfiguration() {
        final DepartmentId departmentId = new DepartmentId(1L);
        final SupplierId supplierId = new SupplierId(11L);
        when(supplierApiService.findDeliveryAreaCouriers(departmentId)).thenReturn(List.of(
                new DeliveryAreaCourierDto(supplierId, null, Set.of("44-251"))));
        when(supplierConfigurationApiService.findBySupplierId(supplierId)).thenReturn(Optional.empty());

        final List<Courier> couriers = adapter.findCouriers(departmentId);

        assertEquals(List.of(new Courier(supplierId, null, Set.of("44-251"), null)), couriers);
    }
}
