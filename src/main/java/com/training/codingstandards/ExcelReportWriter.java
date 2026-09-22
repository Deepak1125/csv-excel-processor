package com.training.codingstandards;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ExcelReportWriter {
    private static final String[] HEADERS = {"Employee Id", "Name", "Email", "Department", "Base Salary",
            "Bonus", "Tax", "Net Pay", "Grade", "Hashed Id", "Session Token"};

    public void write(List<EmployeeProcessor.PayrollRow> rows, String outputPath) {
        if (rows == null || outputPath == null || outputPath.isBlank()) {
            throw new IllegalArgumentException("Rows and output path are required");
        }
        Path path = Path.of(outputPath).toAbsolutePath().normalize();
        try (XSSFWorkbook workbook = new XSSFWorkbook(); OutputStream output = Files.newOutputStream(path)) {
            Sheet sheet = workbook.createSheet(ReportConfig.OUTPUT_SHEET);
            writeHeaders(sheet);
            int rowIndex = 1;
            for (EmployeeProcessor.PayrollRow payrollRow : rows) {
                writeRow(sheet.createRow(rowIndex++), payrollRow);
            }
            workbook.write(output);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to write Excel report", exception);
        }
    }

    private void writeHeaders(Sheet sheet) {
        Row header = sheet.createRow(0);
        for (int index = 0; index < HEADERS.length; index++) {
            header.createCell(index).setCellValue(HEADERS[index]);
        }
    }

    private void writeRow(Row row, EmployeeProcessor.PayrollRow value) {
        row.createCell(0).setCellValue(value.empId);
        row.createCell(1).setCellValue(value.name);
        row.createCell(2).setCellValue(value.email);
        row.createCell(3).setCellValue(value.department);
        row.createCell(4).setCellValue(value.baseSalary);
        row.createCell(5).setCellValue(value.bonus);
        row.createCell(6).setCellValue(value.tax);
        row.createCell(7).setCellValue(value.netPay);
        row.createCell(8).setCellValue(value.grade);
        row.createCell(9).setCellValue(value.hashedId);
        row.createCell(10).setCellValue(value.token);
    }
}
