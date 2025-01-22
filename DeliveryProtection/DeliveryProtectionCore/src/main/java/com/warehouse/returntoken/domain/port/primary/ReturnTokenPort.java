package com.warehouse.returntoken.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.vo.ReturnTokenResponse;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.domain.vo.ReturnTokenRequest;

public interface ReturnTokenPort {
    ReturnTokenResponse signWith(final ReturnTokenRequest request);
    Boolean verify(final ReturnToken returnToken);
    ReturnToken getReturnToken(final ShipmentId shipmentId);
}
