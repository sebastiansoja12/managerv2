package com.warehouse.qrcode.domain.service;

import com.lowagie.text.Image;

import com.warehouse.qrcode.domain.vo.ShipmentId;
import jakarta.servlet.http.HttpServletResponse;

public interface ShipmentExportService {

    void exportToPdf(final HttpServletResponse response, final ShipmentId shipmentId, final Image image) throws Exception;
}
