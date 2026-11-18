package com.warehouse.departmentmap.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.departmentmap.application.port.secondary.DeliveryNetworkServicePort;
import com.warehouse.departmentmap.domain.vo.DepartmentDirection;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.deliverynetwork.api.dto.DepartmentConnectionDto;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DeliveryNetworkServiceAdapter implements DeliveryNetworkServicePort {

    private final DeliveryNetworkApiService deliveryNetworkApiService;
    private final DepartmentApiService departmentApiService;

    public DeliveryNetworkServiceAdapter(final DeliveryNetworkApiService deliveryNetworkApiService,
                                         final DepartmentApiService departmentApiService) {
        this.deliveryNetworkApiService = deliveryNetworkApiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public List<DepartmentDirection> findDirections(final DepartmentId departmentId) {
        return this.deliveryNetworkApiService.getCurrentNetwork().connections().stream()
                .map(connection -> neighbour(connection, departmentId))
                .flatMap(Optional::stream)
                .distinct()
                .map(neighbourId -> this.departmentApiService.findDepartmentCode(neighbourId)
                        .map(departmentCode -> new DepartmentDirection(neighbourId, departmentCode)))
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(direction -> direction.departmentCode().value()))
                .toList();
    }

    private static Optional<DepartmentId> neighbour(final DepartmentConnectionDto connection,
                                                    final DepartmentId departmentId) {
        if (sameDepartment(connection.firstDepartmentId(), departmentId)) {
            return Optional.of(new DepartmentId(connection.secondDepartmentId().value()));
        }
        if (sameDepartment(connection.secondDepartmentId(), departmentId)) {
            return Optional.of(new DepartmentId(connection.firstDepartmentId().value()));
        }
        return Optional.empty();
    }

    private static boolean sameDepartment(final DepartmentIdDto connectionDepartmentId,
                                          final DepartmentId departmentId) {
        return Objects.equals(connectionDepartmentId.value(), departmentId.getValue());
    }
}
