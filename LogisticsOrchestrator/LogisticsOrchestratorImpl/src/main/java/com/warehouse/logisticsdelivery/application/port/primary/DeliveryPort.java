package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.application.port.primary.command.AssignDeliverySupplierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryRelation;
import com.warehouse.logisticsdelivery.application.port.primary.command.AttachDeliveryStepSignatureCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryConditionsCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ChangeDeliveryRouteCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CommentDeliveryStepCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CorrectDeliveryStepStatusCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.ReassignDeliveryStepCarrierCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RegisterDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CompleteDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreateDerivedDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.CreatePalletDeliveryCommand;
import com.warehouse.logisticsdelivery.application.port.primary.command.RecordDeliveryAttemptCommand;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;

import java.util.List;

public interface DeliveryPort {

    void createDelivery(final CreateDeliveryCommand command);

    void createDerivedDelivery(final CreateDerivedDeliveryCommand command);

    void createPalletDelivery(final CreatePalletDeliveryCommand command);

    void completeDelivery(final CompleteDeliveryCommand command);

    DeliverySnapshot recordAttempt(final RecordDeliveryAttemptCommand command);

    void changeDestinationDepartment(final ShipmentId shipmentId, final DepartmentId destinationDepartmentId);

    List<DeliveryRelation> findRelatedDeliveries(final DeliveryId deliveryId);

    DeliverySnapshot assignSupplier(final AssignDeliverySupplierCommand command);

    DeliverySnapshot unassignSupplier(final DeliveryId deliveryId);

    DeliverySnapshot changeRoute(final ChangeDeliveryRouteCommand command);

    DeliverySnapshot changeConditions(final ChangeDeliveryConditionsCommand command);

    DeliverySnapshot registerAttempt(final RegisterDeliveryAttemptCommand command);

    DeliverySnapshot finishDelivery(final DeliveryId deliveryId);

    DeliverySnapshot completeShipmentDelivery(final ShipmentId shipmentId, final boolean recipientSigned);

    DeliverySnapshot commentStep(final CommentDeliveryStepCommand command);

    DeliverySnapshot correctStepStatus(final CorrectDeliveryStepStatusCommand command);

    DeliverySnapshot attachStepSignature(final AttachDeliveryStepSignatureCommand command);

    DeliverySnapshot reassignStepCarrier(final ReassignDeliveryStepCarrierCommand command);

    void cancelDelivery(final DeliveryId deliveryId);

    List<DeliverySnapshot> findRecentDeliveries(final int offset, final int limit);

    List<DeliverySnapshot> findDeliveriesByDepartment(final DepartmentId departmentId, final int offset, final int limit);

    List<DeliverySnapshot> findDeliveriesBySupplier(final SupplierId supplierId, final int offset, final int limit);

    DeliverySnapshot findDelivery(final DeliveryId deliveryId);

    DeliverySnapshot findDeliveryByShipmentId(final ShipmentId shipmentId);

    DeliverySnapshot findDeliveryByPalletId(final String palletId);
}
