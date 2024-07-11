package lecture9.assignment3.util;

import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import lecture9.assignment3.model.Employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PDFGenerator {

    public static ByteArrayInputStream generateEmployeePDF(List<Employee> employees) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Header
        Paragraph header = new Paragraph("Employees Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20);
        document.add(header);

        // Calculations
        Employee highestSalaryEmployee = employees.stream().max((e1, e2) -> e1.getSalary() - e2.getSalary()).orElse(null);
        Employee lowestSalaryEmployee = employees.stream().min((e1, e2) -> e1.getSalary() - e2.getSalary()).orElse(null);
        double averageSalary = employees.stream().mapToInt(Employee::getSalary).average().orElse(0);

        document.add(new Paragraph("Employee with Highest Salary: " + highestSalaryEmployee.getName() + " - " + highestSalaryEmployee.getSalary()));
        document.add(new Paragraph("Employee with Lowest Salary: " + lowestSalaryEmployee.getName() + " - " + lowestSalaryEmployee.getSalary()));
        document.add(new Paragraph("Average Salary: " + averageSalary));

        document.add(new Paragraph("\n")); // Adding space between calculations and table

        // Employee Table
        Table table = new Table(new float[]{1, 3, 3, 3, 3, 2});
        table.setWidth(UnitValue.createPercentValue(100));
        
        table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBackgroundColor(DeviceGray.GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Name")).setBackgroundColor(DeviceGray.GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Date of Birth")).setBackgroundColor(DeviceGray.GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Address")).setBackgroundColor(DeviceGray.GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Department")).setBackgroundColor(DeviceGray.GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Salary")).setBackgroundColor(DeviceGray.GRAY));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Employee employee : employees) {
            table.addCell(new Cell().add(new Paragraph(employee.getId().toString())));
            table.addCell(new Cell().add(new Paragraph(employee.getName())));
            table.addCell(new Cell().add(new Paragraph(dateFormat.format(employee.getDob()))));
            table.addCell(new Cell().add(new Paragraph(employee.getAddress())));
            table.addCell(new Cell().add(new Paragraph(employee.getDepartment())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(employee.getSalary()))));
        }

        document.add(table);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
