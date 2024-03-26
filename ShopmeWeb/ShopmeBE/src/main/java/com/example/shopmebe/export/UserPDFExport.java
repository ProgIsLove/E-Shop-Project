package com.example.shopmebe.export;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPDFExport extends AbstractExporter {

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, ".pdf", "application/pdf", "users_");

        try(Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph paragraph = new Paragraph("List of User", font);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            document.add(paragraph);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10);
            float[] cellSpacing = {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f};
            table.setWidths(cellSpacing);

            writeTableHeader(table);
            writeTableData(table, users);

            document.add(table);
        } catch (IOException ex) {
            throw new IOException();
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        String[] headersName = {"ID", "E-mail", "First name", "Last Name", "Roles", "Enabled"};

        for (String headerName : headersName) {
            cell.setPhrase(new Phrase(headerName, font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table,List<User> users) {
        for (User user : users) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }
}
