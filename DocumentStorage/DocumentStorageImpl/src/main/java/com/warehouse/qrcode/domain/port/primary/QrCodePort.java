package com.warehouse.qrcode.domain.port.primary;

import com.warehouse.qrcode.domain.vo.ShipmentId;
import jakarta.servlet.http.HttpServletResponse;

public interface QrCodePort {

    void exportShipment(final HttpServletResponse response, final ShipmentId shipmentId) throws Exception;
}
