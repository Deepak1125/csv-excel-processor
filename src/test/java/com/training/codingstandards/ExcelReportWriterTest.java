package com.training.codingstandards;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExcelReportWriterTest {
    @Test
    void writesHeadersAndRows() throws Exception {
        Employee employee = new Employee("1", "Name", "name@example.com", "Sales", 70000, 5, "US", "m@example.com");
        EmployeeProcessor.PayrollRow row = new EmployeeProcessor().process(List.of(employee)).get(0);
        Path output = Files.createTempFile("payroll", ".xlsx");
        try {
            new ExcelReportWriter().write(List.of(row), output.toString());
            try (Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook(Files.newInputStream(output))) {
                assertEquals("Payroll", workbook.getSheetAt(0).getSheetName());
                assertEquals("Employee Id", workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue());
                assertEquals("1", workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue());
            }
        } finally {
            Files.deleteIfExists(output);
        }
    }

    @Test
    void validatesArguments() {
        assertThrows(IllegalArgumentException.class, () -> new ExcelReportWriter().write(null, "output.xlsx"));
        assertThrows(IllegalArgumentException.class, () -> new ExcelReportWriter().write(List.of(), ""));
    }
}
