package com.warehouse.qrcode.domain.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ParcelService {

    void exportParcelToPdfById(HttpServletResponse response, Long id) throws Exception;
}
