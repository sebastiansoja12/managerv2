package com.warehouse.qrcode.domain.service;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Utilities;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.vo.LabelParty;

public class PdfShipmentLabelGenerator implements ShipmentLabelGenerator {

    private static final float A6_MARGIN = 14;

    private static final float A4_MARGIN = Utilities.millimetersToPoints(15);

    private static final String EMPTY = "—";

    private static final Color INK = new Color(0x0F, 0x17, 0x2A);

    private static final Color MUTED = new Color(0x64, 0x74, 0x8B);

    private static final Color LINE = new Color(0xE2, 0xE8, 0xF0);

    private static final Color SURFACE = new Color(0xF1, 0xF5, 0xF9);

    private static final Color EXPRESS = new Color(0xDC, 0x26, 0x26);

    private static final Color STANDARD = new Color(0x25, 0x63, 0xEB);

    private static final Color ECONOMY = new Color(0x05, 0x96, 0x69);

    private static final BaseFont REGULAR = baseFont(BaseFont.HELVETICA);

    private static final BaseFont BOLD = baseFont(BaseFont.HELVETICA_BOLD);

    private final LabelFormat format;

    private final Rectangle pageSize;

    private final float margin;

    private final BarcodeGeneratorService barcodeGeneratorService;

    private PdfShipmentLabelGenerator(final LabelFormat format,
                                      final Rectangle pageSize,
                                      final float margin,
                                      final BarcodeGeneratorService barcodeGeneratorService) {
        this.format = format;
        this.pageSize = pageSize;
        this.margin = margin;
        this.barcodeGeneratorService = barcodeGeneratorService;
    }

    public static PdfShipmentLabelGenerator a6(final BarcodeGeneratorService barcodeGeneratorService) {
        return new PdfShipmentLabelGenerator(LabelFormat.PDF_A6, PageSize.A6, A6_MARGIN, barcodeGeneratorService);
    }

    public static PdfShipmentLabelGenerator a4(final BarcodeGeneratorService barcodeGeneratorService) {
        return new PdfShipmentLabelGenerator(LabelFormat.PDF_A4, PageSize.A4, A4_MARGIN, barcodeGeneratorService);
    }

    @Override
    public LabelFormat format() {
        return format;
    }

    @Override
    public String contentType() {
        return "application/pdf";
    }

    @Override
    public String fileExtension() {
        return "pdf";
    }

    @Override
    public void generate(final OutputStream outputStream, final Shipment shipment) throws Exception {
        final String trackingNumber = shipment.trackingNumberValue();

        final Document document = new Document(pageSize, margin, margin, margin, margin);
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, outputStream);
            writer.setCloseStream(false);
            document.addTitle("Etykieta " + valueOrEmpty(trackingNumber));
            document.open();

            document.add(header(shipment));
            document.add(recipient(shipment.recipient()));
            document.add(sender(shipment.sender()));
            document.add(details(shipment));
            document.add(tracking(trackingNumber));
            if (trackingNumber != null) {
                document.add(barcode(writer, trackingNumber));
            }
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }

    private PdfPTable header(final Shipment shipment) {
        final PdfPTable table = table(new float[] { 1.4f, 1f });
        final PdfPCell brand = cell(new Phrase("MANAGER", font(BOLD, 11, Color.WHITE)));
        brand.setBackgroundColor(INK);
        brand.setPadding(8);
        table.addCell(brand);

        final String serviceLevel = shipment.serviceLevel() == null
                ? "PACZKA"
                : shipment.serviceLevel().toUpperCase(Locale.ROOT);
        final PdfPCell badge = cell(new Phrase(serviceLevel, font(BOLD, 10, Color.WHITE)));
        badge.setBackgroundColor(serviceLevelColor(shipment.serviceLevel()));
        badge.setHorizontalAlignment(Element.ALIGN_CENTER);
        badge.setVerticalAlignment(Element.ALIGN_MIDDLE);
        badge.setPadding(8);
        table.addCell(badge);
        return table;
    }

    private PdfPTable recipient(final LabelParty recipient) {
        final PdfPTable table = section();
        table.setSpacingBefore(10);
        final PdfPCell cell = cell(null);
        cell.addElement(caption("ODBIORCA"));
        cell.addElement(line(valueOrEmpty(recipient.destination()), font(BOLD, 20, INK), 22));
        cell.addElement(line(valueOrEmpty(recipient.initials()), font(REGULAR, 10, MUTED), 13));
        bottomRule(cell);
        table.addCell(cell);
        return table;
    }

    private PdfPTable sender(final LabelParty sender) {
        final PdfPTable table = section();
        final PdfPCell cell = cell(null);
        cell.addElement(caption("NADAWCA"));
        cell.addElement(line(valueOrEmpty(sender.origin()), font(REGULAR, 10, INK), 13));
        bottomRule(cell);
        table.addCell(cell);
        return table;
    }

    private PdfPTable details(final Shipment shipment) {
        final PdfPTable table = table(new float[] { 1f, 1.5f, 1f });
        table.setSpacingBefore(8);
        table.addCell(tile("WAGA", valueOrEmpty(shipment.formattedWeight())));
        table.addCell(tile("WYMIARY", valueOrEmpty(shipment.formattedDimensions())));
        table.addCell(tile("PRIORYTET", shipment.priority() == null ? EMPTY : shipment.priority().name()));
        return table;
    }

    private PdfPTable tracking(final String trackingNumber) throws Exception {
        final PdfPTable table = table(new float[] { 1f, 1.55f });
        table.setSpacingBefore(10);

        final PdfPCell qr;
        if (trackingNumber == null) {
            qr = cell(new Phrase(EMPTY, font(BOLD, 12, MUTED)));
        } else {
            final Image qrImage = barcodeGeneratorService.generateQRCodeImage(trackingNumber);
            qr = new PdfPCell(qrImage, true);
            qr.setBorder(Rectangle.NO_BORDER);
        }
        qr.setPaddingRight(10);
        table.addCell(qr);

        final PdfPCell number = cell(null);
        number.setVerticalAlignment(Element.ALIGN_MIDDLE);
        number.addElement(caption("NUMER PRZESYŁKI"));
        number.addElement(line(valueOrEmpty(trackingNumber), font(BOLD, 15, INK), 18));
        number.addElement(line("Zeskanuj, aby śledzić przesyłkę", font(REGULAR, 7.5f, MUTED), 12));
        table.addCell(number);
        return table;
    }

    private PdfPTable barcode(final PdfWriter writer, final String trackingNumber) {
        final Barcode128 barcode = new Barcode128();
        barcode.setCode(trackingNumber);
        barcode.setBarHeight(42);
        barcode.setFont(null);
        final Image image = barcode.createImageWithBarcode(writer.getDirectContent(), INK, INK);
        final PdfPTable table = section();
        table.setSpacingBefore(10);
        final PdfPCell cell = new PdfPCell(image, true);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setFixedHeight(42);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        return table;
    }

    private PdfPCell tile(final String label, final String value) {
        final PdfPCell cell = cell(null);
        cell.setBackgroundColor(SURFACE);
        cell.setBorder(Rectangle.BOX);
        cell.setBorderColor(Color.WHITE);
        cell.setBorderWidth(2);
        cell.setPadding(6);
        cell.setPaddingTop(3);
        cell.addElement(caption(label));
        cell.addElement(line(value, font(BOLD, 9, INK), 12));
        return cell;
    }

    private Paragraph caption(final String text) {
        return line(text, font(BOLD, 6.5f, MUTED), 9);
    }

    private Paragraph line(final String text, final Font font, final float leading) {
        return new Paragraph(leading, text, font);
    }

    private void bottomRule(final PdfPCell cell) {
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColor(LINE);
        cell.setBorderWidth(1);
        cell.setPaddingTop(4);
        cell.setPaddingBottom(8);
    }

    private PdfPTable section() {
        return table(new float[] { 1f });
    }

    private PdfPTable table(final float[] widths) {
        final PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        return table;
    }

    private PdfPCell cell(final Phrase phrase) {
        final PdfPCell cell = phrase == null ? new PdfPCell() : new PdfPCell(phrase);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        return cell;
    }

    private Color serviceLevelColor(final String serviceLevel) {
        if (serviceLevel == null) {
            return MUTED;
        }
        return switch (serviceLevel.toUpperCase(Locale.ROOT)) {
            case "EXPRESS" -> EXPRESS;
            case "ECONOMY" -> ECONOMY;
            default -> STANDARD;
        };
    }

    private String valueOrEmpty(final String value) {
        return value == null || value.isBlank() ? EMPTY : value;
    }

    private Font font(final BaseFont baseFont, final float size, final Color color) {
        return new Font(baseFont, size, Font.NORMAL, color);
    }

    private static BaseFont baseFont(final String name) {
        try {
            return BaseFont.createFont(name, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        } catch (final DocumentException | IOException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
