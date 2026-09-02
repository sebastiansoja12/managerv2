package com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentConnectionApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DepartmentIdApi;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.DeliveryNetworkApiResponse;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.api.ReplaceDeliveryNetworkApiRequest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryNetworkEditorApiMapperTest {

    private final DeliveryNetworkEditorApiMapper mapper = new DeliveryNetworkEditorApiMapper();

    @Test
    void shouldMapTypedRequestToReplacementCommand() {
        final ReplaceDeliveryNetworkApiRequest request = new ReplaceDeliveryNetworkApiRequest(List.of(
                connectionApi(2L, 1L)));

        final ReplaceDeliveryNetworkCommand command = this.mapper.toCommand(request);

        assertEquals(new DepartmentId(2L), command.connections().getFirst().firstDepartmentId());
        assertEquals(new DepartmentId(1L), command.connections().getFirst().secondDepartmentId());
    }

    @Test
    void shouldMapCanonicalConnectionsToSortedResponse() {
        final DeliveryNetworkResult deliveryNetwork = new DeliveryNetworkResult(Set.of(
                connection(3L, 4L),
                connection(1L, 2L)));

        final DeliveryNetworkApiResponse response = this.mapper.toResponse(deliveryNetwork);

        assertEquals(List.of(
                connectionApi(1L, 2L),
                connectionApi(3L, 4L)), response.connections());
    }

    private static DepartmentConnection connection(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnection(new DepartmentId(firstDepartmentId), new DepartmentId(secondDepartmentId));
    }

    private static DepartmentConnectionApi connectionApi(final Long firstDepartmentId, final Long secondDepartmentId) {
        return new DepartmentConnectionApi(
                new DepartmentIdApi(String.valueOf(firstDepartmentId)),
                new DepartmentIdApi(String.valueOf(secondDepartmentId)));
    }
}
