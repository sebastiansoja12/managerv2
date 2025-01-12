package com.warehouse.qrcode.domain.service;

import com.lowagie.text.Image;

import jakarta.servlet.http.HttpServletResponse;

public interface ParcelExportService {

    void exportToPdf(HttpServletResponse response, Long id, Image image) throws Exception;
}
