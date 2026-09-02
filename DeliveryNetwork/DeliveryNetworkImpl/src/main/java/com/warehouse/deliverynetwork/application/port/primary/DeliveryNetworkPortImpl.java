package com.warehouse.deliverynetwork.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.deliverynetwork.application.exception.DepartmentDirectoryImportMismatchException;
import com.warehouse.deliverynetwork.application.exception.IncompleteDepartmentDirectoryImportException;
import com.warehouse.deliverynetwork.application.exception.MissingOperatorContextException;
import com.warehouse.deliverynetwork.application.exception.UnknownDepartmentCodeException;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCodeCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentImportCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentConnectionCodeResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkRepository;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.domain.event.DeliveryNetworkChanged;
import com.warehouse.deliverynetwork.domain.exception.UnknownDepartmentException;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.service.DeliveryPathFinder;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DeliveryNetworkPortImpl implements DeliveryNetworkPort {

    private final DeliveryNetworkRepository deliveryNetworkRepository;

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    private final OperatorContextProvider operatorContextProvider;

    private final DeliveryPathFinder deliveryPathFinder;

    private final DomainEventPublisher domainEventPublisher;

    public DeliveryNetworkPortImpl(
            final DeliveryNetworkRepository deliveryNetworkRepository,
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final OperatorContextProvider operatorContextProvider,
            final DeliveryPathFinder deliveryPathFinder,
            final DomainEventPublisher domainEventPublisher) {
        this.deliveryNetworkRepository = deliveryNetworkRepository;
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.operatorContextProvider = operatorContextProvider;
        this.deliveryPathFinder = deliveryPathFinder;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryNetworkResult getCurrentNetwork() {
        return result(loadCurrentNetwork());
    }

    @Override
    @Transactional
    public DeliveryNetworkResult replaceCurrentNetwork(final ReplaceDeliveryNetworkCommand command) {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        final List<DepartmentNode> departments = this.departmentDirectoryServicePort.getCurrentOperatorDepartments();
        final List<DepartmentConnection> connections = command.connections()
                .stream()
                .map(this::connection)
                .toList();

        return replace(deliveryNetwork, departments, connections);
    }

    @Override
    @Transactional
    public DeliveryNetworkResult replaceCurrentNetworkByDepartmentCodes(
            final ReplaceDeliveryNetworkByCodesCommand command) {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        final List<DepartmentNode> departments = this.departmentDirectoryServicePort.getCurrentOperatorDepartments();
        final Map<String, DepartmentNode> departmentsByCode = indexDepartmentsByCode(departments);
        validateImportedDepartments(command.departments(), departments, departmentsByCode);
        final List<DepartmentConnection> connections = command.connections()
                .stream()
                .map(connection -> connection(connection, departmentsByCode))
                .toList();

        return replace(deliveryNetwork, departments, connections);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryNetworkExportResult getCurrentNetworkForExport() {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        final List<DepartmentNode> departments = this.departmentDirectoryServicePort.getCurrentOperatorDepartments();
        final Map<DepartmentId, DepartmentCode> departmentCodesById = indexDepartmentCodesById(departments);
        final List<DepartmentExportResult> departmentResults = departments.stream()
                .map(department -> new DepartmentExportResult(
                        department.departmentCode(),
                        department.departmentType(),
                        department.status()))
                .toList();
        final List<DepartmentConnectionCodeResult> connections = deliveryNetwork.connections()
                .stream()
                .map(connection -> new DepartmentConnectionCodeResult(
                        departmentCode(connection.firstDepartmentId(), departmentCodesById),
                        departmentCode(connection.secondDepartmentId(), departmentCodesById)))
                .toList();

        return new DeliveryNetworkExportResult(departmentResults, connections);
    }

    @Override
    @Transactional
    public void removeDepartmentConnections(final DepartmentId departmentId) {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        if (!deliveryNetwork.removeConnectionsFor(departmentId)) {
            return;
        }
        this.deliveryNetworkRepository.save(deliveryNetwork);
        this.domainEventPublisher.publish(new DeliveryNetworkChanged(deliveryNetwork.snapshot(), Instant.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean areDirectlyConnected(
            final DepartmentId firstDepartmentId,
            final DepartmentId secondDepartmentId) {
        return loadCurrentNetwork().directlyConnects(firstDepartmentId, secondDepartmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryPath findDeliveryPath(
            final DepartmentId sourceDepartmentId,
            final DepartmentId targetDepartmentId) {
        return this.deliveryPathFinder.findShortestPath(
                loadCurrentNetwork(), sourceDepartmentId, targetDepartmentId);
    }

    private DeliveryNetwork loadCurrentNetwork() {
        final OperatorId operatorId = currentOperatorId();
        return this.deliveryNetworkRepository.find()
                .orElseGet(() -> DeliveryNetwork.empty(operatorId));
    }

    private OperatorId currentOperatorId() {
        return this.operatorContextProvider.currentOperatorId()
                .orElseThrow(MissingOperatorContextException::new);
    }

    private DepartmentConnection connection(final DepartmentConnectionCommand connection) {
        return new DepartmentConnection(connection.firstDepartmentId(), connection.secondDepartmentId());
    }

    private DepartmentConnection connection(
            final DepartmentConnectionCodeCommand connection,
            final Map<String, DepartmentNode> departmentsByCode) {
        return new DepartmentConnection(
                department(connection.firstDepartmentCode(), departmentsByCode).departmentId(),
                department(connection.secondDepartmentCode(), departmentsByCode).departmentId());
    }

    private DepartmentNode department(
            final DepartmentCode departmentCode,
            final Map<String, DepartmentNode> departmentsByCode) {
        final DepartmentNode department = departmentsByCode.get(normalizedCode(departmentCode));
        if (department == null) {
            throw new UnknownDepartmentCodeException(departmentCode);
        }
        return department;
    }

    private Map<String, DepartmentNode> indexDepartmentsByCode(final List<DepartmentNode> departments) {
        final Map<String, DepartmentNode> departmentsByCode = new HashMap<>();
        for (final DepartmentNode department : departments) {
            final String normalizedCode = normalizedCode(department.departmentCode());
            if (departmentsByCode.put(normalizedCode, department) != null) {
                throw new IllegalArgumentException(
                        "Department directory contains duplicate code: " + department.departmentCode().getValue());
            }
        }
        return departmentsByCode;
    }

    private void validateImportedDepartments(
            final List<DepartmentImportCommand> importedDepartments,
            final List<DepartmentNode> departments,
            final Map<String, DepartmentNode> departmentsByCode) {
        final Set<String> importedCodes = importedDepartments.stream()
                .map(importedDepartment -> {
                    final DepartmentNode department = department(
                            importedDepartment.departmentCode(), departmentsByCode);
                    if (department.departmentType() != importedDepartment.departmentType()
                            || department.status() != importedDepartment.status()) {
                        throw new DepartmentDirectoryImportMismatchException(importedDepartment.departmentCode());
                    }
                    return normalizedCode(importedDepartment.departmentCode());
                })
                .collect(Collectors.toSet());
        final List<DepartmentCode> missingDepartmentCodes = departments.stream()
                .map(DepartmentNode::departmentCode)
                .filter(departmentCode -> !importedCodes.contains(normalizedCode(departmentCode)))
                .toList();
        if (!missingDepartmentCodes.isEmpty()) {
            throw new IncompleteDepartmentDirectoryImportException(missingDepartmentCodes);
        }
    }

    private Map<DepartmentId, DepartmentCode> indexDepartmentCodesById(final List<DepartmentNode> departments) {
        final Map<DepartmentId, DepartmentCode> departmentCodesById = new HashMap<>();
        for (final DepartmentNode department : departments) {
            if (departmentCodesById.put(department.departmentId(), department.departmentCode()) != null) {
                throw new IllegalArgumentException(
                        "Department directory contains duplicate ID: " + department.departmentId().getValue());
            }
        }
        return departmentCodesById;
    }

    private DepartmentCode departmentCode(
            final DepartmentId departmentId,
            final Map<DepartmentId, DepartmentCode> departmentCodesById) {
        final DepartmentCode departmentCode = departmentCodesById.get(departmentId);
        if (departmentCode == null) {
            throw new UnknownDepartmentException(departmentId);
        }
        return departmentCode;
    }

    private String normalizedCode(final DepartmentCode departmentCode) {
        return departmentCode.getValue().trim().toUpperCase(Locale.ROOT);
    }

    private DeliveryNetworkResult replace(
            final DeliveryNetwork deliveryNetwork,
            final List<DepartmentNode> departments,
            final List<DepartmentConnection> connections) {
        deliveryNetwork.replaceConnections(connections, departments);
        this.deliveryNetworkRepository.save(deliveryNetwork);
        this.domainEventPublisher.publish(new DeliveryNetworkChanged(deliveryNetwork.snapshot(), Instant.now()));
        return result(deliveryNetwork);
    }

    private DeliveryNetworkResult result(final DeliveryNetwork deliveryNetwork) {
        return new DeliveryNetworkResult(deliveryNetwork.connections());
    }
}
