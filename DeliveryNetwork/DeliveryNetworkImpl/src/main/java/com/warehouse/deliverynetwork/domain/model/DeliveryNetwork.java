package com.warehouse.deliverynetwork.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.deliverynetwork.domain.exception.DuplicateDepartmentConnectionException;
import com.warehouse.deliverynetwork.domain.exception.MissingSortingFacilityConnectionException;
import com.warehouse.deliverynetwork.domain.exception.UnavailableDepartmentException;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryNetwork {

    private final OperatorId operatorId;

    private Set<DepartmentConnection> connections;

    public DeliveryNetwork(final OperatorId operatorId, final Set<DepartmentConnection> connections) {
        this.operatorId = Objects.requireNonNull(operatorId, "Operator ID cannot be null");
        Objects.requireNonNull(operatorId.getValue(), "Operator ID value cannot be null");
        this.connections = Set.copyOf(Objects.requireNonNull(connections, "Connections cannot be null"));
    }

    public static DeliveryNetwork empty(final OperatorId operatorId) {
        return new DeliveryNetwork(operatorId, Set.of());
    }

    public void replaceConnections(final Collection<DepartmentConnection> proposedConnections,
                                   final Collection<DepartmentNode> departments) {
        Objects.requireNonNull(proposedConnections, "Proposed connections cannot be null");
        Objects.requireNonNull(departments, "Departments cannot be null");

        final Map<DepartmentId, DepartmentNode> departmentsById = indexDepartments(departments);
        final Set<DepartmentConnection> uniqueConnections = uniqueConnections(proposedConnections);

        validateConnectionDepartments(uniqueConnections, departmentsById);
        validateSortingFacilityConnections(uniqueConnections, departmentsById.values());

        this.connections = Set.copyOf(uniqueConnections);
    }

    public boolean directlyConnects(final DepartmentId firstDepartmentId, final DepartmentId secondDepartmentId) {
        return this.connections.contains(new DepartmentConnection(firstDepartmentId, secondDepartmentId));
    }

    public boolean removeConnectionsFor(final DepartmentId departmentId) {
        Objects.requireNonNull(departmentId, "Department ID cannot be null");
        final Set<DepartmentConnection> remainingConnections = this.connections.stream()
                .filter(connection -> !connection.connects(departmentId))
                .collect(Collectors.toSet());
        if (remainingConnections.size() == this.connections.size()) {
            return false;
        }
        this.connections = Set.copyOf(remainingConnections);
        return true;
    }

    public OperatorId operatorId() {
        return this.operatorId;
    }

    public Set<DepartmentConnection> connections() {
        return this.connections;
    }

    public DeliveryNetworkSnapshot snapshot() {
        return new DeliveryNetworkSnapshot(this.operatorId, this.connections);
    }

    private Map<DepartmentId, DepartmentNode> indexDepartments(final Collection<DepartmentNode> departments) {
        final Map<DepartmentId, DepartmentNode> departmentsById = new HashMap<>();
        for (final DepartmentNode department : departments) {
            final DepartmentNode previousDepartment = departmentsById.put(department.departmentId(), department);
            if (previousDepartment != null) {
                throw new IllegalArgumentException(
                        "Department directory contains duplicate ID: " + department.departmentId().getValue());
            }
        }
        return departmentsById;
    }

    private Set<DepartmentConnection> uniqueConnections(
            final Collection<DepartmentConnection> proposedConnections) {
        final Set<DepartmentConnection> uniqueConnections = new HashSet<>();
        for (final DepartmentConnection connection : proposedConnections) {
            Objects.requireNonNull(connection, "Department connection cannot be null");
            if (!uniqueConnections.add(connection)) {
                throw new DuplicateDepartmentConnectionException(connection);
            }
        }
        return uniqueConnections;
    }

    private void validateConnectionDepartments(
            final Set<DepartmentConnection> proposedConnections,
            final Map<DepartmentId, DepartmentNode> departmentsById) {
        for (final DepartmentConnection connection : proposedConnections) {
            validateConnectionDepartment(connection.firstDepartmentId(), departmentsById);
            validateConnectionDepartment(connection.secondDepartmentId(), departmentsById);
        }
    }

    private void validateConnectionDepartment(
            final DepartmentId departmentId,
            final Map<DepartmentId, DepartmentNode> departmentsById) {
        final DepartmentNode department = departmentsById.get(departmentId);
        if (department == null) {
            throw new UnknownDepartmentException(departmentId);
        }
        if (!department.participatesInDeliveryNetwork()) {
            throw new UnavailableDepartmentException(departmentId);
        }
    }

    private void validateSortingFacilityConnections(
            final Set<DepartmentConnection> proposedConnections,
            final Collection<DepartmentNode> departments) {
        final Set<DepartmentId> sortingFacilityIds = new HashSet<>();
        for (final DepartmentNode department : departments) {
            if (department.participatesInDeliveryNetwork() && department.isSortingFacility()) {
                sortingFacilityIds.add(department.departmentId());
            }
        }

        final List<DepartmentCode> missingDepartmentCodes = departments.stream()
                .filter(DepartmentNode::participatesInDeliveryNetwork)
                .filter(department -> !department.isSortingFacility())
                .filter(department -> !hasSortingFacilityConnection(
                        department.departmentId(), proposedConnections, sortingFacilityIds))
                .map(DepartmentNode::departmentCode)
                .sorted(Comparator.comparing(DepartmentCode::getValue))
                .toList();

        if (!missingDepartmentCodes.isEmpty()) {
            throw new MissingSortingFacilityConnectionException(missingDepartmentCodes);
        }
    }

    private boolean hasSortingFacilityConnection(
            final DepartmentId departmentId,
            final Set<DepartmentConnection> proposedConnections,
            final Set<DepartmentId> sortingFacilityIds) {
        for (final DepartmentConnection connection : proposedConnections) {
            if (!connection.connects(departmentId)) {
                continue;
            }
            if (sortingFacilityIds.contains(connection.firstDepartmentId())
                    || sortingFacilityIds.contains(connection.secondDepartmentId())) {
                return true;
            }
        }
        return false;
    }
}
