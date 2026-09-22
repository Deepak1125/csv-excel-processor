package com.training.codingstandards;

import java.util.List;

public final class App {

    private App() {
        // Utility entry point.
    }

    public static void main(String[] args) {
        String csvPath = args.length > 0 ? args[0] : null;
        String excelPath = args.length > 1 ? args[1] : "payroll-report.xlsx";

        System.out.println("CSV to Excel processor starting...");
        CsvEmployeeReader reader = new CsvEmployeeReader();
        List<Employee> employees = reader.read(csvPath);

        EmployeeProcessor processor = new EmployeeProcessor();
        List<EmployeeProcessor.PayrollRow> rows = processor.process(employees);
        new ExcelReportWriter().write(rows, excelPath);

        if (args.length > 2) {
            DatabaseHelper database = new DatabaseHelper();
            database.auditExport(args[2]);
            if (args.length > 3) {
                Employee lookedUp = database.findEmployee(args[3]);
                System.out.println("Lookup result: " + (lookedUp == null ? "not found" : lookedUp.getName()));
            }
        }
        System.out.println("Processed " + rows.size() + " employees into " + excelPath);
    }
}
