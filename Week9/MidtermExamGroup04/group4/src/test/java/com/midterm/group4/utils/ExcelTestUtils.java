package com.midterm.group4.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelTestUtils {

    public static MultipartFile createDummyExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Product Name");
        headerRow.createCell(1).setCellValue("Price");
        headerRow.createCell(2).setCellValue("Quantity");
        headerRow.createCell(3).setCellValue("Active");

        Row dataRow1 = sheet.createRow(1);
        dataRow1.createCell(0).setCellValue("Product A");
        dataRow1.createCell(1).setCellValue(100);
        dataRow1.createCell(2).setCellValue(50);
        dataRow1.createCell(3).setCellValue(true);

        Row dataRow2 = sheet.createRow(2);
        dataRow2.createCell(0).setCellValue("Product B");
        dataRow2.createCell(1).setCellValue(150);
        dataRow2.createCell(2).setCellValue(30);
        dataRow2.createCell(3).setCellValue(false);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", new ByteArrayInputStream(out.toByteArray()));
    }
}
