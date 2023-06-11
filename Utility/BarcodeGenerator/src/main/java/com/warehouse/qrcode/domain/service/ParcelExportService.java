package com.warehouse.qrcode.domain.service;

import com.warehouse.shipment.domain.model.Parcel;

import jakarta.servlet.http.HttpServletResponse;

public interface ParcelExportService {

    void exportToPdf(HttpServletResponse response, Parcel parcel) throws Exception;
}
