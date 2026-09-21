package com.warehouse.deliverynetwork.application.listener;

import com.warehouse.commonassets.helper.FieldDifference;
import com.warehouse.deliverynetwork.application.port.secondary.AuditLogServicePort;
import com.warehouse.deliverynetwork.domain.enumeration.DeliveryNetworkAuditLogType;
import com.warehouse.deliverynetwork.domain.event.DeliveryNetworkChanged;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkAuditLog;
import com.warehouse.deliverynetwork.domain.vo.DeliveryNetworkSnapshot;
import com.warehouse.deliverynetwork.domain.vo.DepartmentConnection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(
        name = {"manager.kafka.integration-events.enabled", "manager.kafka.outbox.enabled"},
        havingValue = "true")
public class DeliveryNetworkAuditLogListener {

    private static final String CONNECTION_FIELD = "connection";

    private final AuditLogServicePort auditLogServicePort;

    public DeliveryNetworkAuditLogListener(final AuditLogServicePort auditLogServicePort) {
        this.auditLogServicePort = auditLogServicePort;
    }

    @EventListener
    public void handle(final DeliveryNetworkChanged event) {
        final DeliveryNetworkSnapshot snapshot = event.getSnapshot();
        final Set<DepartmentConnection> previousConnections = event.getPreviousSnapshot()
                .map(DeliveryNetworkSnapshot::connections)
                .orElseGet(Set::of);
        this.auditLogServicePort.record(new DeliveryNetworkAuditLog(snapshot.operatorId(),
                DeliveryNetworkAuditLogType.CONNECTIONS_CHANGED,
                changes(previousConnections, snapshot.connections())));
    }

    private List<FieldDifference> changes(final Set<DepartmentConnection> previousConnections,
                                          final Set<DepartmentConnection> currentConnections) {
        return Stream.concat(
                        currentConnections.stream()
                                .filter(connection -> !previousConnections.contains(connection))
                                .map(connection -> new FieldDifference(CONNECTION_FIELD, null, format(connection))),
                        previousConnections.stream()
                                .filter(connection -> !currentConnections.contains(connection))
                                .map(connection -> new FieldDifference(CONNECTION_FIELD, format(connection), null)))
                .toList();
    }

    private String format(final DepartmentConnection connection) {
        return connection.firstDepartmentId().getValue() + "-" + connection.secondDepartmentId().getValue();
    }
}
