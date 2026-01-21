package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.*;
import com.warehouse.shipment.domain.model.ShipmentUpdateCommand;
import com.warehouse.shipment.domain.vo.*;

public interface ShipmentPort {

    Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateCommand request);

    Result<Void, ErrorCode> update(final ShipmentUpdateCommand request);

    void changeSenderTo(final ShipmentId shipmentId, final Sender sender);

    void changeRecipientTo(final ShipmentId shipmentId, final Recipient recipient);

    void changePersonTo(final Person person, final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ChangeShipmentTypeRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod);

    void changeIssuerCountryTo(final ShipmentCountryRequest request);

    void changeReceiverCountryTo(final ShipmentCountryRequest request);

    void changeShipmentCountries(final ShipmentCountryRequest request);

    Shipment loadShipment(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    Result<Void, ErrorCode> addDangerousGood(final DangerousGoodCreateCommand request);

    void processShipmentReturn(final ShipmentReturnCommand request);

    void processShipmentDelivery(final ShipmentDeliveryCommand command);
}
