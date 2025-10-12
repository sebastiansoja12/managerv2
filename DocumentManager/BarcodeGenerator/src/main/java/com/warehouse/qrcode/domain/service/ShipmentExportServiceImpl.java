package com.warehouse.qrcode.domain.service;

import java.awt.*;

import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.secondary.ShipmentRepository;
import com.warehouse.qrcode.domain.vo.ShipmentId;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ShipmentExportServiceImpl implements ShipmentExportService {

    private final ShipmentRepository shipmentRepository;

    private void writeTableHeader(final PdfPTable table, final String... headers) {
        final PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setPadding(5);

        final Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);

        for (final String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }

    private void writeTableData(final PdfPTable table, final String... data) {
        for (final String value : data) {
            final PdfPCell cell = new PdfPCell(new Phrase(value != null ? value : ""));
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    @Override
    public void exportToPdf(final HttpServletResponse response, final ShipmentId shipmentId, final Image qrImage) throws Exception {
        final Document document = new Document(PageSize.LETTER);
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setCloseStream(false);
            document.open();

            final Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLUE);
            final Paragraph header = new Paragraph("ETYKIETA PACZKI", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(15);
            document.add(header);

            final Shipment shipment = shipmentRepository.find(shipmentId);

            final PdfPTable senderTable = new PdfPTable(2);
            senderTable.setWidthPercentage(100);
            senderTable.setSpacingBefore(10);
            writeTableHeader(senderTable, "Nadawca", "Dane");
            writeTableData(senderTable,
                    shipment.getFirstName() + " " + shipment.getLastName(),
                    shipment.getSenderStreet() + ", " + shipment.getSenderCity() + "\nTel: " + shipment.getSenderTelephone()
            );
            document.add(senderTable);

            final PdfPTable recipientTable = new PdfPTable(2);
            recipientTable.setWidthPercentage(100);
            recipientTable.setSpacingBefore(10);
            writeTableHeader(recipientTable, "Odbiorca", "Dane");
            writeTableData(recipientTable,
                    shipment.getRecipientFirstName() + " " + shipment.getRecipientLastName(),
                    shipment.getRecipientStreet() + ", " + shipment.getRecipientCity() +
                            "\nTel: " + shipment.getRecipientTelephone() +
                            "\nEmail: " + shipment.getRecipientEmail()
            );
            document.add(recipientTable);

            final PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);
            writeTableHeader(infoTable, "Numer przesylki", "Informacje dodatkowe");
            writeTableData(infoTable,
                    String.valueOf(shipment.getId()),
                    "Waga: " + "xx" + " kg\nTyp: " + "xx"
            );
            document.add(infoTable);

            final Paragraph qrParagraph = new Paragraph("Skanuj w celu sledzenia:");
            qrParagraph.setSpacingBefore(15);
            qrParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(qrParagraph);

            qrImage.setAlignment(Image.ALIGN_CENTER);
            qrImage.scalePercent(150);
            document.add(qrImage);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }
    }
}
