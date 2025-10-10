package com.warehouse.dangerousgood.domain.model;

import com.warehouse.commonassets.enumeration.WeightUnit;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Weight;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;
import com.warehouse.dangerousgood.infrastructure.adapter.primary.api.DangerousGoodChangeWeightApiRequest;

public class DangerousGoodChangeWeightRequest {
    private ShipmentId shipmentId;
    private DangerousGoodId dangerousGoodId;
    private Weight weight;

	public DangerousGoodChangeWeightRequest(final DangerousGoodId dangerousGoodId, final ShipmentId shipmentId,
			final Weight weight) {
        this.dangerousGoodId = dangerousGoodId;
        this.shipmentId = shipmentId;
        this.weight = weight;
    }

    public static DangerousGoodChangeWeightRequest from(final DangerousGoodChangeWeightApiRequest request) {
        final ShipmentId shipmentId = new ShipmentId(request.shipmentId().getValue());
        final DangerousGoodId dangerousGoodId = new DangerousGoodId(request.dangerousGoodId().value());
        final Weight weight = new Weight(request.weight().value(), WeightUnit.valueOf(request.weight().unit()));
        return new DangerousGoodChangeWeightRequest(dangerousGoodId, shipmentId, weight);
    }

    public DangerousGoodId getDangerousGoodId() {
        return dangerousGoodId;
    }

    public void setDangerousGoodId(final DangerousGoodId dangerousGoodId) {
        this.dangerousGoodId = dangerousGoodId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(final ShipmentId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(final Weight weight) {
        this.weight = weight;
    }
}
