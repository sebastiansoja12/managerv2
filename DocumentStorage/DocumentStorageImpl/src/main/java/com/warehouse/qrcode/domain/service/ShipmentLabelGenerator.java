package com.warehouse.qrcode.domain.service;

import java.io.OutputStream;

import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.model.Shipment;

public interface ShipmentLabelGenerator {

    LabelFormat format();

    String contentType();

    String fileExtension();

    void generate(final OutputStream outputStream, final Shipment shipment) throws Exception;
}
