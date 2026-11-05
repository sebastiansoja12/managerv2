package com.warehouse.deliverynetwork.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;

import java.util.Locale;

public interface DeliveryNetworkPort {

    DeliveryNetworkResult getCurrentNetwork();

    DeliveryNetworkResult replaceCurrentNetwork(final ReplaceDeliveryNetworkCommand command);

    DeliveryNetworkResult replaceCurrentNetworkByDepartmentCodes(final ReplaceDeliveryNetworkByCodesCommand command);

    DeliveryNetworkExportResult getCurrentNetworkForExport();

    void removeDepartmentConnections(final DepartmentId departmentId);

    void connectDepartmentToSortingFacilities(final DepartmentId departmentId);

    boolean canActivateDepartment(final DepartmentId departmentId);

    boolean areDirectlyConnected(final DepartmentId firstDepartmentId, final DepartmentId secondDepartmentId);

    DeliveryPath findDeliveryPath(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId);

    ShipmentDeliveryRouteResult findShipmentDeliveryRoute(final TrackingNumber trackingNumber);

    ShipmentRouteDocumentFile generateShipmentRouteDocument(final TrackingNumber trackingNumber, final Locale locale);

    ShipmentRouteDocumentFile generateShipmentHandlingPlan(final TrackingNumber trackingNumber, final Locale locale);
}
