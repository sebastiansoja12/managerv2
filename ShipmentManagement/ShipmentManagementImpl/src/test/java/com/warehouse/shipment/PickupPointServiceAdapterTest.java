package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.api.PickupPointApiService;
import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionDto;
import com.warehouse.pickuppoint.api.dto.PickupPointStatusDto;
import com.warehouse.pickuppoint.api.dto.PickupPointTypeDto;
import com.warehouse.shipment.infrastructure.adapter.secondary.PickupPointServiceAdapter;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PickupPointServiceAdapterTest {

    @Test
    void shouldReturnDepartmentAssignedToPickupPoint() {
        final PickupPointApiService pickupPointApiService = mock(PickupPointApiService.class);
        final PickupPointServiceAdapter adapter = new PickupPointServiceAdapter(pickupPointApiService);
        final PickupPointId pickupPointId = PickupPointId.generate();
        final PickupPointIdDto pickupPointIdDto = new PickupPointIdDto(pickupPointId.value());
        final PickupPointSelectionDto pickupPoint = new PickupPointSelectionDto(
                pickupPointIdDto, "PP1", "Pickup point", PickupPointTypeDto.SERVICE_POINT,
                PickupPointStatusDto.ACTIVE, Set.of(), null, null, new DepartmentIdDto(11L), 1L, Instant.now());
        when(pickupPointApiService.getById(pickupPointIdDto)).thenReturn(Optional.of(pickupPoint));

        final Optional<DepartmentId> departmentId = adapter.findDepartmentId(pickupPointId);

        assertEquals(Optional.of(new DepartmentId(11L)), departmentId);
    }

    @Test
    void shouldReturnEmptyWhenPickupPointDoesNotExist() {
        final PickupPointApiService pickupPointApiService = mock(PickupPointApiService.class);
        final PickupPointServiceAdapter adapter = new PickupPointServiceAdapter(pickupPointApiService);
        final PickupPointId pickupPointId = PickupPointId.generate();
        when(pickupPointApiService.getById(new PickupPointIdDto(pickupPointId.value())))
                .thenReturn(Optional.empty());

        final Optional<DepartmentId> departmentId = adapter.findDepartmentId(pickupPointId);

        assertTrue(departmentId.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenPickupPointHasNoDepartment() {
        final PickupPointApiService pickupPointApiService = mock(PickupPointApiService.class);
        final PickupPointServiceAdapter adapter = new PickupPointServiceAdapter(pickupPointApiService);
        final PickupPointId pickupPointId = PickupPointId.generate();
        final PickupPointIdDto pickupPointIdDto = new PickupPointIdDto(pickupPointId.value());
        final PickupPointSelectionDto pickupPoint = new PickupPointSelectionDto(
                pickupPointIdDto, "PP1", "Pickup point", PickupPointTypeDto.SERVICE_POINT,
                PickupPointStatusDto.ACTIVE, Set.of(), null, null, null, 1L, Instant.now());
        when(pickupPointApiService.getById(pickupPointIdDto)).thenReturn(Optional.of(pickupPoint));

        final Optional<DepartmentId> departmentId = adapter.findDepartmentId(pickupPointId);

        assertTrue(departmentId.isEmpty());
    }
}
