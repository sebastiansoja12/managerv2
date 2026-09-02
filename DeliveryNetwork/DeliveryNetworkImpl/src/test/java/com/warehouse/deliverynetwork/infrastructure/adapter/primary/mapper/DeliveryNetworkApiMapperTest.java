package com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.api.dto.DepartmentConnectionDto;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryNetworkApiMapperTest {

    private final DeliveryNetworkApiMapper deliveryNetworkApiMapper = new DeliveryNetworkApiMapper();

    @Test
    void shouldMapConnectionsInCanonicalOrder() {
        final DeliveryNetworkResult deliveryNetwork = new DeliveryNetworkResult(Set.of(
                connection(3L, 4L),
                connection(1L, 2L)));

        final DeliveryNetworkDto deliveryNetworkDto = this.deliveryNetworkApiMapper.toDto(deliveryNetwork);

        assertEquals(List.of(
                new DepartmentConnectionDto(new DepartmentIdDto(1L), new DepartmentIdDto(2L)),
                new DepartmentConnectionDto(new DepartmentIdDto(3L), new DepartmentIdDto(4L))
        ), deliveryNetworkDto.connections());
    }

    @Test
    void shouldMapOrderedDeliveryPath() {
        final DeliveryPath deliveryPath = new DeliveryPath(List.of(
                new DepartmentId(1L),
                new DepartmentId(2L),
                new DepartmentId(3L)));

        final DeliveryPathDto deliveryPathDto = this.deliveryNetworkApiMapper.toDto(deliveryPath);

        assertEquals(
                List.of(new DepartmentIdDto(1L), new DepartmentIdDto(2L), new DepartmentIdDto(3L)),
                deliveryPathDto.departmentIds());
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(new DepartmentId(firstDepartmentId), new DepartmentId(secondDepartmentId));
    }
}
