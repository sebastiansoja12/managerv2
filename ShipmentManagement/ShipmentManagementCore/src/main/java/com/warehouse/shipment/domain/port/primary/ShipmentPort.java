package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.SignatureMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.DangerousGoodCreateRequest;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.ShipmentCreateRequest;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.*;

public interface ShipmentPort {

    Result<ShipmentCreateResponse, ErrorCode> ship(final ShipmentCreateRequest request);

    Result<Void, ErrorCode> update(final ShipmentUpdateRequest request);

    void changeSenderTo(final ShipmentCreateRequest request);

    void changeRecipientTo(final ShipmentCreateRequest request);

    void changePersonTo(final Person person, final ShipmentId shipmentId);

    void changeShipmentTypeTo(final ChangeShipmentTypeRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request, final SignatureMethod signatureMethod);

    void changeOriginCountryTo(final ShipmentCountryRequest request);

    void changeDestinationCountryTo(final ShipmentCountryRequest request);

    void changeShipmentCountries(final ShipmentCountryRequest request);

    Shipment loadShipment(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    Result<Void, ErrorCode> addDangerousGood(final ShipmentId shipmentId, final DangerousGoodCreateRequest request);
}
