package com.warehouse.deliverynetwork.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkCommand;
import com.warehouse.deliverynetwork.application.port.primary.command.ReplaceDeliveryNetworkByCodesCommand;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkExportResult;
import com.warehouse.deliverynetwork.application.port.primary.result.DeliveryNetworkResult;
import com.warehouse.deliverynetwork.domain.vo.DeliveryPath;

public interface DeliveryNetworkPort {

    DeliveryNetworkResult getCurrentNetwork();

    DeliveryNetworkResult replaceCurrentNetwork(final ReplaceDeliveryNetworkCommand command);

    DeliveryNetworkResult replaceCurrentNetworkByDepartmentCodes(final ReplaceDeliveryNetworkByCodesCommand command);

    DeliveryNetworkExportResult getCurrentNetworkForExport();

    void removeDepartmentConnections(final DepartmentId departmentId);

    boolean areDirectlyConnected(final DepartmentId firstDepartmentId, final DepartmentId secondDepartmentId);

    DeliveryPath findDeliveryPath(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId);
}
