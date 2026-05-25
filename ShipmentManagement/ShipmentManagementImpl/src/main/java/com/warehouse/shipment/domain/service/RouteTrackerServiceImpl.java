package com.warehouse.shipment.domain.service;

import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.port.secondary.SoftwareConfigurationServicePort;
import com.warehouse.shipment.domain.vo.*;

public class RouteTrackerServiceImpl implements RouteTrackerService {

    private final RouteLogServicePort routeLogServicePort;

    private final SoftwareConfigurationServicePort softwareConfigurationServicePort;

    private final CurrentUserServicePort currentUserServicePort;

    public RouteTrackerServiceImpl(final RouteLogServicePort routeLogServicePort,
                                   final SoftwareConfigurationServicePort softwareConfigurationServicePort,
                                   final CurrentUserServicePort currentUserServicePort) {
        this.routeLogServicePort = routeLogServicePort;
        this.softwareConfigurationServicePort = softwareConfigurationServicePort;
        this.currentUserServicePort = currentUserServicePort;
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentCreated(final ShipmentId shipmentId) {
        final SoftwareConfiguration softwareConfiguration = this.softwareConfigurationServicePort.getShipmentSoftwareConfiguration();
        final UserContext userContext = currentUserServicePort.getCurrentUserContext();
        return this.routeLogServicePort.notifyShipmentCreated(shipmentId, softwareConfiguration, userContext.userId());
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentRecipientChanged(final ShipmentId shipmentId, final Recipient recipient) {
        final SoftwareConfiguration softwareConfiguration = this.softwareConfigurationServicePort.getShipmentPersonSoftwareConfiguration();
        final RouteProcess routeProcess = this.routeLogServicePort.notifyPersonChanged(shipmentId, recipient, softwareConfiguration);

        if (routeProcess != null) {
            return Result.success(routeProcess);
        }

        return Result.failure(ErrorCode.ROUTE_TRACKER_SERVICE_NOT_AVAILABLE);
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentPersonChanged(final ShipmentId shipmentId, final Person person) {
        final SoftwareConfiguration softwareConfiguration = this.softwareConfigurationServicePort.getShipmentPersonSoftwareConfiguration();
        final RouteProcess routeProcess = this.routeLogServicePort.notifyPersonChanged(shipmentId, person, softwareConfiguration);

        if (routeProcess != null) {
            return Result.success(routeProcess);
        }

        return Result.failure(ErrorCode.ROUTE_TRACKER_SERVICE_NOT_AVAILABLE);
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentSenderChanged(final ShipmentId shipmentId, final Sender sender) {
        return null;
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentStatusChanged(final ShipmentId shipmentId, final ShipmentStatus shipmentStatus) {
        return Result.success();
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentSent(final ShipmentId shipmentId) {
        return null;
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentDelivered(final ShipmentId shipmentId) {
        return null;
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentLocked(final ShipmentId shipmentId) {
        return null;
    }

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentCurrencyChanged(final ShipmentId shipmentId, final Currency currency) {
        return null;
    }
}
