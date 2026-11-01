package com.warehouse.qrcode.domain.service;

import java.io.IOException;

import com.google.zxing.WriterException;
import com.lowagie.text.Image;

public interface BarcodeGeneratorService {

    Image generateQRCodeImage(final String barcodeText) throws WriterException, IOException;
}
