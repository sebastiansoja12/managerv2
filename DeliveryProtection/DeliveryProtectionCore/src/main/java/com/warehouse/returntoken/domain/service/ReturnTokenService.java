package com.warehouse.returntoken.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.model.ReturnPackageRequest;
import com.warehouse.returntoken.domain.vo.ReturnPackageResponse;
import com.warehouse.returntoken.domain.vo.ReturnToken;

public interface ReturnTokenService {
    ReturnPackageResponse sign(final ReturnPackageRequest returnTokenDetails);
    ReturnToken findByShipmentId(final ShipmentId shipmentId);
    Boolean exists(final ReturnToken returnToken);
}
