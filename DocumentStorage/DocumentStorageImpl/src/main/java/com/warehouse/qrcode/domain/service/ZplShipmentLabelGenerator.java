package com.warehouse.qrcode.domain.service;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.model.Shipment;

public class ZplShipmentLabelGenerator implements ShipmentLabelGenerator {

    private static final int LABEL_WIDTH_DOTS = 812;

    private static final int LABEL_LENGTH_DOTS = 1218;

    private static final String EMPTY = "-";

    @Override
    public LabelFormat format() {
        return LabelFormat.ZPL;
    }

    @Override
    public String contentType() {
        return "text/plain;charset=UTF-8";
    }

    @Override
    public String fileExtension() {
        return "zpl";
    }

    @Override
    public void generate(final OutputStream outputStream, final Shipment shipment) throws Exception {
        outputStream.write(render(shipment).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    private String render(final Shipment shipment) {
        final String trackingNumber = shipment.trackingNumberValue();
        final StringBuilder zpl = new StringBuilder()
                .append("^XA\n")
                .append("^CI28\n")
                .append("^PW").append(LABEL_WIDTH_DOTS).append('\n')
                .append("^LL").append(LABEL_LENGTH_DOTS).append('\n')
                .append(text(40, 40, 44, "MANAGER"))
                .append(text(520, 40, 44, serviceLevel(shipment)))
                .append("^FO40,110^GB732,3,3^FS\n")
                .append(text(40, 140, 24, "ODBIORCA"))
                .append(text(40, 175, 64, shipment.recipient().destination()))
                .append(text(40, 250, 32, shipment.recipient().initials()))
                .append(text(40, 320, 24, "NADAWCA"))
                .append(text(40, 355, 32, shipment.sender().origin()))
                .append("^FO40,410^GB732,3,3^FS\n")
                .append(text(40, 440, 24, "WAGA"))
                .append(text(40, 475, 32, shipment.formattedWeight()))
                .append(text(280, 440, 24, "WYMIARY"))
                .append(text(280, 475, 32, shipment.formattedDimensions()))
                .append(text(580, 440, 24, "PRIORYTET"))
                .append(text(580, 475, 32, shipment.priority() == null ? null : shipment.priority().name()))
                .append(text(360, 600, 24, "NUMER PRZESYŁKI"))
                .append(text(360, 640, 40, trackingNumber));
        if (trackingNumber != null) {
            zpl.append("^FO40,560^BQN,2,7^FDQA,").append(sanitize(trackingNumber)).append("^FS\n")
                    .append("^FO60,900^BY3^BCN,200,N,N,N^FD").append(sanitize(trackingNumber)).append("^FS\n");
        }
        return zpl.append("^XZ\n").toString();
    }

    private String text(final int x, final int y, final int height, final String value) {
        return "^FO" + x + "," + y + "^A0N," + height + "," + height + "^FD" + sanitize(valueOrEmpty(value)) + "^FS\n";
    }

    private String serviceLevel(final Shipment shipment) {
        return shipment.serviceLevel() == null ? "PACZKA" : shipment.serviceLevel().toUpperCase(Locale.ROOT);
    }

    private String sanitize(final String value) {
        return value.replace('^', ' ').replace('~', ' ');
    }

    private String valueOrEmpty(final String value) {
        return value == null || value.isBlank() ? EMPTY : value;
    }
}
