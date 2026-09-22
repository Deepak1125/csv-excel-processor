package com.training.codingstandards;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeProcessor {

    public List<PayrollRow> process(List<Employee> employees) {
        if (employees == null) {
            return List.of();
        }
        List<PayrollRow> rows = new ArrayList<>(employees.size());
        for (Employee employee : employees) {
            if (employee == null) {
                throw new IllegalArgumentException("Employee list must not contain null values");
            }
            rows.add(createPayrollRow(employee));
        }
        return rows;
    }

    private PayrollRow createPayrollRow(Employee employee) {
        PayrollRow row = new PayrollRow();
        row.empId = employee.empId;
        row.name = employee.name;
        row.department = employee.department;
        row.email = employee.email;
        row.baseSalary = employee.salary;
        row.hashedId = SecurityUtil.hashIdentifier(employee.empId + employee.email);
        row.bonus = calculateBonus(employee);
        row.tax = calculateTax(employee.salary, employee.country);
        row.netPay = employee.salary + row.bonus - row.tax;
        row.grade = grade(employee.salary, employee.yearsOfService, employee.department);
        row.token = SecurityUtil.sessionToken();
        return row;
    }

    private double calculateBonus(Employee employee) {
        String department = employee.department == null ? "" : employee.department;
        if (Objects.equals(department, "Engineering")) {
            if (employee.yearsOfService > 10) {
                if (employee.salary > 100000) {
                    return employee.salary * (isAsianCountry(employee.country)
                            ? 0.18 : employee.salary > 110000 ? 0.15 : 0.12);
                }
                return employee.salary * (employee.yearsOfService > 12 ? 0.14 : 0.10);
            }
            return employee.salary * (employee.yearsOfService > 5
                    ? employee.salary > 90000 ? 0.10 : 0.08 : 0.05);
        }
        if (Objects.equals(department, "Finance")) {
            return employee.salary * (employee.yearsOfService > 5
                    ? employee.salary > 80000 ? 0.09 : 0.07 : 0.04);
        }
        if (Objects.equals(department, "Sales")) {
            return employee.salary * (employee.yearsOfService > 4 ? 0.11 : 0.06);
        }
        return employee.salary * (employee.yearsOfService > 3 ? 0.05 : 0.03);
    }

    private boolean isAsianCountry(String country) {
        return Objects.equals(country, "JP") || Objects.equals(country, "SG");
    }

    private double calculateTax(double salary, String country) {
        if (Objects.equals(country, "IN")) {
            return salary * (salary > 100000 ? 0.30 : salary > 70000 ? 0.20 : 0.10);
        }
        if (Objects.equals(country, "US")) {
            return salary * (salary > 100000 ? 0.28 : salary > 70000 ? 0.18 : 0.12);
        }
        if (Objects.equals(country, "SG")) {
            return salary * 0.15;
        }
        if (Objects.equals(country, "JP")) {
            return salary * 0.20;
        }
        return salary * 0.10;
    }

    private String grade(double salary, int years, String department) {
        if (salary > 100000) {
            return years > 8 && Objects.equals(department, "Engineering") ? "L5" : "L4";
        }
        if (salary > 80000) {
            return years > 5 ? "L3" : "L2";
        }
        return salary > 60000 ? "L2" : "L1";
    }

    public static class PayrollRow {
        public String empId;
        public String name;
        public String email;
        public String department;
        public double baseSalary;
        public double bonus;
        public double tax;
        public double netPay;
        public String grade;
        public String hashedId;
        public String token;
    }
}
