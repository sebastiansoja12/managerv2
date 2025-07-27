package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ShipmentErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.model.DangerousGoodCreateRequest;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.model.SignatureChangeRequest;
import com.warehouse.shipment.domain.vo.*;

public interface ShipmentPort {

    Result<ShipmentCreateResponse, ShipmentErrorCode> ship(final ShipmentCreateRequest request);

    Result<Void, ShipmentErrorCode> update(final ShipmentUpdateRequest request);

    void changeSenderTo(final ShipmentCreateRequest request);

    void changeRecipientTo(final ShipmentCreateRequest request);

    void changeShipmentTypeTo(final ShipmentCreateRequest request);

    void changeShipmentStatusTo(final ShipmentStatusRequest request);

    void changeShipmentSignatureTo(final SignatureChangeRequest request);

    void changeOriginCountryTo(final ShipmentCountryRequest request);

    void changeDestinationCountryTo(final ShipmentCountryRequest request);

    void changeShipmentCountries(final ShipmentCountryRequest request);

    Shipment loadShipment(final ShipmentId shipmentId);

    boolean existsShipment(final ShipmentId shipmentId);

    Result<Void, ShipmentErrorCode> addDangerousGood(final ShipmentId shipmentId, final DangerousGoodCreateRequest request);
}
