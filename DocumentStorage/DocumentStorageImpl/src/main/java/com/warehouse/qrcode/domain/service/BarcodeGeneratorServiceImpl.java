package com.warehouse.qrcode.domain.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.lowagie.text.Image;
import net.glxn.qrgen.QRCode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class BarcodeGeneratorServiceImpl implements BarcodeGeneratorService {

    @Override
    public Image getQRCodeImage(Long text) throws WriterException, IOException, BadElementException {
        final QRCodeWriter qrCodeWriter = new QRCodeWriter();
        final BitMatrix bitMatrix = qrCodeWriter.encode(String.valueOf(text), BarcodeFormat.QR_CODE, 400, 400);
        final ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return Image.getInstance(String.valueOf(bitMatrix));
    }

    @Override
    public Image generateQRCodeImage(String barcodeText) throws IOException {
        final BufferedImage image = new BufferedImage(250,
                250,
                BufferedImage.TYPE_BYTE_GRAY);
        final ByteArrayOutputStream stream = QRCode
                .from(barcodeText)
                .withSize(250, 250)
                .stream();

        ImageIO.write(image, "png", stream);

        return Image.getInstance(stream.toByteArray());
    }
}
