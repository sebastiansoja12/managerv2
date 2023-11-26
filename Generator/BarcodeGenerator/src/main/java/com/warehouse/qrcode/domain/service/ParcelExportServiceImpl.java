package com.warehouse.qrcode.domain.service;

import java.awt.*;

import com.warehouse.qrcode.domain.model.Parcel;
import com.warehouse.qrcode.domain.port.secondary.ParcelRepository;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ParcelExportServiceImpl implements ParcelExportService {

    private final ParcelRepository parcelRepository;

    private void writeTableHeader(PdfPTable senderTable, PdfPTable recipientTable) {
        final PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.gray);
        cell.setPadding(2);

        final com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Kod", font));
        senderTable.addCell(cell);

        cell.setPhrase(new Phrase("Dane nadawcy", font));
        senderTable.addCell(cell);

        cell.setPhrase(new Phrase("Numer telefonu nadawcy", font));
        senderTable.addCell(cell);


        cell.setPhrase(new Phrase("Dane odbiorcy", font));
        recipientTable.addCell(cell);

        cell.setPhrase(new Phrase("Numer telefonu", font));
        recipientTable.addCell(cell);

        cell.setPhrase(new Phrase("Miasto", font));
        recipientTable.addCell(cell);

        cell.setPhrase(new Phrase("Ulica", font));
        recipientTable.addCell(cell);

        cell.setPhrase(new Phrase("Email odbiorcy", font));
        recipientTable.addCell(cell);

        cell.setPhrase(new Phrase("Kod QR", font));
        recipientTable.addCell(cell);

    }


	private void writeTableData(PdfPTable senderTable, PdfPTable recipientTable, Parcel parcel, Image image) {
        //sender
        senderTable.addCell(String.valueOf(parcel.getId()));
        senderTable.addCell(parcel.getFirstName() + " " + parcel.getLastName());
        senderTable.addCell(String.valueOf(parcel.getSenderTelephone()));

        //recipient
        recipientTable.addCell(parcel.getRecipientFirstName() + " " + parcel.getRecipientLastName());
        recipientTable.addCell(String.valueOf(parcel.getRecipientTelephone()));
        recipientTable.addCell((parcel.getRecipientCity()));
        recipientTable.addCell((parcel.getRecipientStreet()));
        recipientTable.addCell(String.valueOf(parcel.getRecipientEmail()));
        recipientTable.addCell(image);
    }

	@Override
	public void exportToPdf(HttpServletResponse response, Long id, Image image) throws Exception {
        final Document document = new Document(PageSize.LETTER);
        final PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        writer.setCloseStream(false);

        document.open();
        final Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        final PdfPTable senderTable = new PdfPTable(3);
        final PdfPTable recipientTable = new PdfPTable(6);


        senderTable.setWidthPercentage(100f);
        senderTable.setSpacingBefore(10);

        recipientTable.setWidthPercentage(100f);
        recipientTable.setSpacingBefore(10);


        writeTableHeader(senderTable, recipientTable);

        final Parcel parcel = parcelRepository.find(id);

        writeTableData(senderTable, recipientTable, parcel, image);

        document.add(senderTable);
        document.add(recipientTable);
        document.close();

        writer.close();

    }
}

