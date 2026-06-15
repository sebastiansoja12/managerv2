package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.exception.DeliveryPathNotFoundException;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DeliveryPathFinder {

    public DeliveryPath findShortestPath(final DeliveryNetwork deliveryNetwork,
                                         final DepartmentId sourceDepartmentId,
                                         final DepartmentId targetDepartmentId) {
        Objects.requireNonNull(deliveryNetwork, "Delivery network cannot be null");
        Objects.requireNonNull(sourceDepartmentId, "Source department ID cannot be null");
        Objects.requireNonNull(targetDepartmentId, "Target department ID cannot be null");

        if (sourceDepartmentId.equals(targetDepartmentId)) {
            return new DeliveryPath(List.of(sourceDepartmentId));
        }

        final Map<DepartmentId, Set<DepartmentId>> adjacency = adjacency(deliveryNetwork.connections());
        final Deque<DepartmentId> departmentsToVisit = new ArrayDeque<>();
        final Set<DepartmentId> visitedDepartments = new HashSet<>();
        final Map<DepartmentId, DepartmentId> previousDepartments = new HashMap<>();

        departmentsToVisit.add(sourceDepartmentId);
        visitedDepartments.add(sourceDepartmentId);

        while (!departmentsToVisit.isEmpty()) {
            final DepartmentId currentDepartmentId = departmentsToVisit.removeFirst();
            for (final DepartmentId connectedDepartmentId : sortedNeighbours(adjacency, currentDepartmentId)) {
                if (!visitedDepartments.add(connectedDepartmentId)) {
                    continue;
                }

                previousDepartments.put(connectedDepartmentId, currentDepartmentId);
                if (connectedDepartmentId.equals(targetDepartmentId)) {
                    return buildPath(sourceDepartmentId, targetDepartmentId, previousDepartments);
                }
                departmentsToVisit.addLast(connectedDepartmentId);
            }
        }

        throw new DeliveryPathNotFoundException(sourceDepartmentId, targetDepartmentId);
    }

    private Map<DepartmentId, Set<DepartmentId>> adjacency(final Set<DepartmentConnection> connections) {
        final Map<DepartmentId, Set<DepartmentId>> adjacency = new HashMap<>();
        for (final DepartmentConnection connection : connections) {
            adjacency.computeIfAbsent(connection.firstDepartmentId(), ignored -> new HashSet<>())
                    .add(connection.secondDepartmentId());
            adjacency.computeIfAbsent(connection.secondDepartmentId(), ignored -> new HashSet<>())
                    .add(connection.firstDepartmentId());
        }
        return adjacency;
    }

    private List<DepartmentId> sortedNeighbours(
            final Map<DepartmentId, Set<DepartmentId>> adjacency,
            final DepartmentId departmentId) {
        final List<DepartmentId> neighbours = new ArrayList<>(adjacency.getOrDefault(departmentId, Set.of()));
        neighbours.sort(Comparator.comparing(DepartmentId::getValue));
        return neighbours;
    }

    private DeliveryPath buildPath(
            final DepartmentId sourceDepartmentId,
            final DepartmentId targetDepartmentId,
            final Map<DepartmentId, DepartmentId> previousDepartments) {
        final LinkedList<DepartmentId> departmentIds = new LinkedList<>();
        DepartmentId currentDepartmentId = targetDepartmentId;
        departmentIds.addFirst(currentDepartmentId);

        while (!currentDepartmentId.equals(sourceDepartmentId)) {
            currentDepartmentId = previousDepartments.get(currentDepartmentId);
            departmentIds.addFirst(currentDepartmentId);
        }

        return new DeliveryPath(departmentIds);
    }
}
