package com.training.codingstandards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvEmployeeReaderTest {
    @Test
    void readsBundledEmployeeCsv() {
        List<Employee> employees = new CsvEmployeeReader().read(null);
        assertEquals(8, employees.size());
        assertEquals("1001", employees.get(0).empId);
    }

    @Test
    void missingFileIsReported() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new CsvEmployeeReader().read("does-not-exist.csv"));
        assertTrue(exception.getMessage().contains("Unable to read"));
    }
}
