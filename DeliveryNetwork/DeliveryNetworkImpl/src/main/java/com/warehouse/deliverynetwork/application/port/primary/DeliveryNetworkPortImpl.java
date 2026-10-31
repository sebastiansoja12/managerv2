package com.warehouse.deliverynetwork.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.deliverynetwork.application.port.primary.command.DepartmentConnectionCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentConnectionCodeResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DepartmentExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.application.port.secondary.DeliveryNetworkRepository;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.application.port.secondary.ShipmentDeliveryServicePort;
import com.warehouse.deliverynetwork.application.service.DepartmentDirectory;
import com.warehouse.deliverynetwork.application.service.ShipmentDeliveryRouteFactory;
import com.warehouse.deliverynetwork.application.service.ShipmentRouteDocumentService;
import com.warehouse.deliverynetwork.domain.event.DeliveryNetworkChanged;
import com.warehouse.deliverynetwork.domain.model.DeliveryNetwork;
import com.warehouse.deliverynetwork.domain.service.DeliveryPathFinder;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class DeliveryNetworkPortImpl implements DeliveryNetworkPort {

    private final DeliveryNetworkRepository deliveryNetworkRepository;

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    private final OperatorContextProvider operatorContextProvider;

    private final DeliveryPathFinder deliveryPathFinder;

    private final DomainEventPublisher domainEventPublisher;

    private final ShipmentDeliveryServicePort shipmentDeliveryServicePort;

    private final ShipmentRouteDocumentService shipmentRouteDocumentService;

    private final ShipmentDeliveryRouteFactory shipmentDeliveryRouteFactory;

    public DeliveryNetworkPortImpl(
            final DeliveryNetworkRepository deliveryNetworkRepository,
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final OperatorContextProvider operatorContextProvider,
            final DeliveryPathFinder deliveryPathFinder,
            final DomainEventPublisher domainEventPublisher,
            final ShipmentDeliveryServicePort shipmentDeliveryServicePort,
            final ShipmentRouteDocumentService shipmentRouteDocumentService,
            final ShipmentDeliveryRouteFactory shipmentDeliveryRouteFactory) {
        this.deliveryNetworkRepository = deliveryNetworkRepository;
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.operatorContextProvider = operatorContextProvider;
        this.deliveryPathFinder = deliveryPathFinder;
        this.domainEventPublisher = domainEventPublisher;
        this.shipmentDeliveryServicePort = shipmentDeliveryServicePort;
        this.shipmentRouteDocumentService = shipmentRouteDocumentService;
        this.shipmentDeliveryRouteFactory = shipmentDeliveryRouteFactory;
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
        final DepartmentDirectory directory = currentDepartmentDirectory();
        directory.validateImport(command.departments());
        final List<DepartmentConnection> connections = command.connections()
                .stream()
                .map(directory::connection)
                .toList();

        return replace(deliveryNetwork, directory.departments(), connections);
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryNetworkExportResult getCurrentNetworkForExport() {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        final DepartmentDirectory directory = currentDepartmentDirectory();
        final List<DepartmentExportResult> departmentResults = directory.departments().stream()
                .map(department -> new DepartmentExportResult(
                        department.departmentCode(),
                        department.departmentType(),
                        department.status()))
                .toList();
        final List<DepartmentConnectionCodeResult> connections = deliveryNetwork.connections()
                .stream()
                .map(connection -> new DepartmentConnectionCodeResult(
                        directory.departmentCode(connection.firstDepartmentId()),
                        directory.departmentCode(connection.secondDepartmentId())))
                .toList();

        return new DeliveryNetworkExportResult(departmentResults, connections);
    }

    @Override
    @Transactional
    public void removeDepartmentConnections(final DepartmentId departmentId) {
        final DeliveryNetwork deliveryNetwork = loadCurrentNetwork();
        final DeliveryNetworkSnapshot previousSnapshot = deliveryNetwork.snapshot();
        if (!deliveryNetwork.removeConnectionsFor(departmentId)) {
            return;
        }
        this.deliveryNetworkRepository.save(deliveryNetwork);
        this.domainEventPublisher.publish(new DeliveryNetworkChanged(previousSnapshot, deliveryNetwork.snapshot(),
                Instant.now()));
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
        return this.deliveryPathFinder.findShortestPath(loadCurrentNetwork(),
                this.departmentDirectoryServicePort.getCurrentOperatorDepartments(),
                sourceDepartmentId, targetDepartmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentDeliveryRouteResult findShipmentDeliveryRoute(final TrackingNumber trackingNumber) {
        return this.shipmentDeliveryRouteFactory.create(trackingNumber,
                this.shipmentDeliveryServicePort.getShipmentDepartments(trackingNumber),
                this::loadCurrentNetwork,
                this.departmentDirectoryServicePort::getCurrentOperatorDepartments);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentRouteDocumentFile generateShipmentRouteDocument(final TrackingNumber trackingNumber, final Locale locale) {
        return this.shipmentRouteDocumentService.routeDocument(findShipmentDeliveryRoute(trackingNumber), locale);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentRouteDocumentFile generateShipmentHandlingPlan(final TrackingNumber trackingNumber, final Locale locale) {
        return this.shipmentRouteDocumentService.handlingPlan(findShipmentDeliveryRoute(trackingNumber), locale);
    }

    private DeliveryNetwork loadCurrentNetwork() {
        final OperatorId operatorId = currentOperatorId();
        return this.deliveryNetworkRepository.find()
                .orElseGet(() -> DeliveryNetwork.empty(operatorId));
    }

    private OperatorId currentOperatorId() {
        return this.operatorContextProvider.currentOperatorId();
    }

    private DepartmentConnection connection(final DepartmentConnectionCommand connection) {
        return new DepartmentConnection(connection.firstDepartmentId(), connection.secondDepartmentId());
    }

    private DepartmentDirectory currentDepartmentDirectory() {
        return new DepartmentDirectory(this.departmentDirectoryServicePort.getCurrentOperatorDepartments());
    }

    private DeliveryNetworkResult replace(
            final DeliveryNetwork deliveryNetwork,
            final List<DepartmentNode> departments,
            final List<DepartmentConnection> connections) {
        final DeliveryNetworkSnapshot previousSnapshot = deliveryNetwork.snapshot();
        deliveryNetwork.replaceConnections(connections, departments);
        this.deliveryNetworkRepository.save(deliveryNetwork);
        this.domainEventPublisher.publish(new DeliveryNetworkChanged(previousSnapshot, deliveryNetwork.snapshot(),
                Instant.now()));
        return result(deliveryNetwork);
    }

    private DeliveryNetworkResult result(final DeliveryNetwork deliveryNetwork) {
        return new DeliveryNetworkResult(deliveryNetwork.connections());
    }
}
