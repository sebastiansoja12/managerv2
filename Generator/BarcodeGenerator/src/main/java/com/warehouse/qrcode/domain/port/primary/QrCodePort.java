package com.warehouse.qrcode.domain.port.primary;

import jakarta.servlet.http.HttpServletResponse;

public interface QrCodePort {

    void exportParcelToPdfById(HttpServletResponse response, Long id) throws Exception;
}
