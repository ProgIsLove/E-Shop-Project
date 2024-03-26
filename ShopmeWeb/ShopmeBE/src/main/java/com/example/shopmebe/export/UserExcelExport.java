package com.example.shopmebe.export;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExport extends AbstractExporter {
    private static final XSSFWorkbook WORKBOOK = new XSSFWorkbook();
    private XSSFSheet sheet;

    private void writeHeaderLine() {
        sheet = WORKBOOK.createSheet("Users");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = setStyle(true, 16);

        createCell(row, 0, "User Id", cellStyle);
        createCell(row, 1, "E-mail", cellStyle);
        createCell(row, 2, "First Name", cellStyle);
        createCell(row, 3, "Last Name", cellStyle);
        createCell(row, 4, "Roles", cellStyle);
        createCell(row, 5, "Enabled", cellStyle);
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer integerValue) {
            cell.setCellValue(integerValue);

        } else if (value instanceof Boolean booleanValue ){
            cell.setCellValue(booleanValue);
        } else {
            cell.setCellValue(value.toString());
        }
        cell.setCellStyle(cellStyle);
    }

    private void writeDataLines(List<User> users) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = setStyle(false, 14);

        for (User user : users) {
            XSSFRow row = sheet.createRow(rowIndex++);
            int columnIndex = 0;

            createCell(row, columnIndex++, user.getId(), cellStyle);
            createCell(row, columnIndex++, user.getEmail(), cellStyle);
            createCell(row, columnIndex++, user.getFirstName(), cellStyle);
            createCell(row, columnIndex++, user.getLastName(), cellStyle);
            createCell(row, columnIndex++, user.getRoles(), cellStyle);
            createCell(row, columnIndex, user.isEnabled(), cellStyle);
        }
    }

    private XSSFCellStyle setStyle(boolean bold, int fontSize) {
        XSSFCellStyle cellStyle = WORKBOOK.createCellStyle();
        XSSFFont font = WORKBOOK.createFont();
        font.setBold(bold);
        font.setFontHeight(fontSize);
        cellStyle.setFont(font);

        return cellStyle;
    }

    public void export(List<User> users, HttpServletResponse response) throws IOException {
        setResponseHeader(response, ".xlsx", "application/octet-stream", "users_");

        try (WORKBOOK;
             ServletOutputStream outputStream = response.getOutputStream();
        ) {
            writeHeaderLine();
            writeDataLines(users);
            WORKBOOK.write(outputStream);
        }
        catch (IOException ex) {
            throw new IOException("writing to file failed");
        }
    }
}
