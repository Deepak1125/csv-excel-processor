package com.training.codingstandards;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeProcessorTest {
    private final EmployeeProcessor processor = new EmployeeProcessor();

    @Test
    void nullEmployeesProduceEmptyRows() {
        assertTrue(processor.process(null).isEmpty());
    }

    @Test
    void processCalculatesEngineeringPayroll() {
        Employee employee = employee("1001", "Engineering", 92000, 6, "IN");
        EmployeeProcessor.PayrollRow row = processor.process(List.of(employee)).get(0);
        assertEquals(9200, row.bonus);
        assertEquals(18400, row.tax);
        assertEquals("L3", row.grade);
        assertEquals(82800, row.netPay);
        assertEquals(64, row.hashedId.length());
        assertEquals(64, row.token.length());
    }

    @Test
    void processCoversDepartmentAndCountryRules() {
        assertEquals(0.18 * 120000, row("Engineering", 120000, 11, "JP").bonus);
        assertEquals(0.14 * 90000, row("Engineering", 90000, 13, "IN").bonus);
        assertEquals(0.09 * 90000, row("Finance", 90000, 6, "US").bonus);
        assertEquals(0.11 * 70000, row("Sales", 70000, 5, "SG").bonus);
        assertEquals(0.03 * 50000, row("Support", 50000, 2, "CA").bonus);
        assertEquals(0.28 * 120000, row("Finance", 120000, 1, "US").tax);
        assertEquals(0.15 * 120000, row("Finance", 120000, 1, "SG").tax);
        assertEquals(0.20 * 120000, row("Finance", 120000, 1, "JP").tax);
    }

    @Test
    void nullEmployeeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> processor.process(Arrays.asList((Employee) null)));
    }

    private EmployeeProcessor.PayrollRow row(String department, double salary, int years, String country) {
        return processor.process(List.of(employee("1", department, salary, years, country))).get(0);
    }

    private Employee employee(String id, String department, double salary, int years, String country) {
        return new Employee(id, "Name", "name@example.com", department, salary, years, country, "manager@example.com");
    }
}
