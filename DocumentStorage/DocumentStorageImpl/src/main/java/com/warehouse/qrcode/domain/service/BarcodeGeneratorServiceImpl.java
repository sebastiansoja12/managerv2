package com.warehouse.qrcode.domain.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Image;

public class BarcodeGeneratorServiceImpl implements BarcodeGeneratorService {

    private static final int QR_SIZE = 400;

    @Override
    public Image generateQRCodeImage(final String barcodeText) throws WriterException, IOException {
        final BitMatrix bitMatrix = new QRCodeWriter().encode(barcodeText, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE,
                Map.of(EncodeHintType.MARGIN, 0, EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M));
        final ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return Image.getInstance(pngOutputStream.toByteArray());
    }
}
