package com.warehouse.qrcode.domain.service;

import com.google.zxing.WriterException;
import com.itextpdf.text.BadElementException;
import com.lowagie.text.Image;

import java.io.IOException;

public interface BarcodeGeneratorService {

    Image getQRCodeImage(Long text) throws WriterException, IOException, BadElementException;

    Image generateQRCodeImage(Long barcodeText) throws BadElementException, IOException;
}
