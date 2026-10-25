package com.warehouse.deliverynetwork.domain.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.exception.DeliveryPathNotFoundException;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

public class DeliveryPathFinder {

    public DeliveryPath findShortestPath(final DeliveryNetwork deliveryNetwork,
                                         final Collection<DepartmentNode> departments,
                                         final DepartmentId sourceDepartmentId,
                                         final DepartmentId targetDepartmentId) {
        Objects.requireNonNull(deliveryNetwork, "Delivery network cannot be null");
        Objects.requireNonNull(departments, "Departments cannot be null");
        Objects.requireNonNull(sourceDepartmentId, "Source department ID cannot be null");
        Objects.requireNonNull(targetDepartmentId, "Target department ID cannot be null");

        if (sourceDepartmentId.equals(targetDepartmentId)) {
            return new DeliveryPath(List.of(sourceDepartmentId));
        }

        final Map<DepartmentId, DepartmentNode> departmentsById = indexDepartments(departments);
        final DepartmentNode sourceDepartment = department(sourceDepartmentId, departmentsById);
        department(targetDepartmentId, departmentsById);

        final Map<DepartmentId, Set<DepartmentId>> adjacency = adjacency(deliveryNetwork.connections());
        final Map<DepartmentId, RouteCost> bestCosts = new HashMap<>();
        final Map<DepartmentId, DepartmentId> previousDepartments = new HashMap<>();
        final Set<DepartmentId> settledDepartments = new HashSet<>();
        final PriorityQueue<RouteStep> stepsToVisit = new PriorityQueue<>(Comparator
                .comparing(RouteStep::cost, RouteCost.ORDER)
                .thenComparing(step -> step.department().departmentId().getValue()));

        bestCosts.put(sourceDepartmentId, RouteCost.START);
        stepsToVisit.add(new RouteStep(sourceDepartment, RouteCost.START));

        while (!stepsToVisit.isEmpty()) {
            final RouteStep currentStep = stepsToVisit.poll();
            final DepartmentNode currentDepartment = currentStep.department();
            if (!settledDepartments.add(currentDepartment.departmentId())) {
                continue;
            }
            if (currentDepartment.departmentId().equals(targetDepartmentId)) {
                return buildPath(sourceDepartmentId, targetDepartmentId, previousDepartments);
            }
            for (final DepartmentId connectedDepartmentId
                    : adjacency.getOrDefault(currentDepartment.departmentId(), Set.of())) {
                final DepartmentNode connectedDepartment = departmentsById.get(connectedDepartmentId);
                if (connectedDepartment == null
                        || settledDepartments.contains(connectedDepartmentId)
                        || !connectedDepartment.participatesInDeliveryNetwork()) {
                    continue;
                }

                final RouteCost connectedCost = currentStep.cost().next(
                        isBranchTransfer(connectedDepartment, targetDepartmentId),
                        currentDepartment.distanceInKilometersTo(connectedDepartment));
                final RouteCost bestCost = bestCosts.get(connectedDepartmentId);
                if (bestCost == null || RouteCost.ORDER.compare(connectedCost, bestCost) < 0) {
                    bestCosts.put(connectedDepartmentId, connectedCost);
                    previousDepartments.put(connectedDepartmentId, currentDepartment.departmentId());
                    stepsToVisit.add(new RouteStep(connectedDepartment, connectedCost));
                }
            }
        }

        throw new DeliveryPathNotFoundException(sourceDepartmentId, targetDepartmentId);
    }

    private boolean isBranchTransfer(final DepartmentNode department, final DepartmentId targetDepartmentId) {
        return !department.departmentId().equals(targetDepartmentId) && !department.isSortingFacility();
    }

    private Map<DepartmentId, DepartmentNode> indexDepartments(final Collection<DepartmentNode> departments) {
        final Map<DepartmentId, DepartmentNode> departmentsById = new HashMap<>();
        for (final DepartmentNode department : departments) {
            departmentsById.put(department.departmentId(), department);
        }
        return departmentsById;
    }

    private DepartmentNode department(final DepartmentId departmentId,
                                      final Map<DepartmentId, DepartmentNode> departmentsById) {
        final DepartmentNode department = departmentsById.get(departmentId);
        if (department == null) {
            throw new UnknownDepartmentException(departmentId);
        }
        return department;
    }

    private Map<DepartmentId, Set<DepartmentId>> adjacency(final Set<DepartmentConnection> connections) {
        final Map<DepartmentId, Set<DepartmentId>> adjacency = new HashMap<>();
        for (final DepartmentConnection connection : connections) {
            adjacency.computeIfAbsent(connection.firstDepartmentId(), ignored -> new HashSet<>())
                    .add(connection.secondDepartmentId());
            if (connection.bidirectional()) {
                adjacency.computeIfAbsent(connection.secondDepartmentId(), ignored -> new HashSet<>())
                        .add(connection.firstDepartmentId());
            }
        }
        return adjacency;
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

    private record RouteStep(DepartmentNode department, RouteCost cost) {
    }

    private record RouteCost(int branchTransfers, double distanceInKilometers, int legs) {

        private static final RouteCost START = new RouteCost(0, 0, 0);

        private static final Comparator<RouteCost> ORDER = Comparator
                .comparingInt(RouteCost::branchTransfers)
                .thenComparingDouble(RouteCost::distanceInKilometers)
                .thenComparingInt(RouteCost::legs);

        private RouteCost next(final boolean branchTransfer, final double legDistanceInKilometers) {
            return new RouteCost(
                    this.branchTransfers + (branchTransfer ? 1 : 0),
                    this.distanceInKilometers + legDistanceInKilometers,
                    this.legs + 1);
        }
    }
}
