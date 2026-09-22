package com.training.codingstandards;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvEmployeeReader {

    public List<Employee> read(String csvPath) {
        try (InputStream input = openInput(csvPath);
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build().parse(reader)) {
            List<Employee> employees = new ArrayList<>();
            for (CSVRecord record : parser) {
                employees.add(toEmployee(record));
            }
            return employees;
        } catch (IOException | IllegalArgumentException exception) {
            throw new IllegalStateException("Unable to read employee CSV", exception);
        }
    }

    private InputStream openInput(String csvPath) throws IOException {
        if (csvPath == null || csvPath.isBlank()) {
            InputStream resource = CsvEmployeeReader.class.getResourceAsStream("/employees.csv");
            if (resource == null) {
                throw new IOException("Bundled employees.csv was not found");
            }
            return resource;
        }
        return Files.newInputStream(Path.of(csvPath));
    }

    private Employee toEmployee(CSVRecord record) {
        return new Employee(record.get("empId"), record.get("name"), record.get("email"),
                record.get("department"), Double.parseDouble(record.get("salary")),
                Integer.parseInt(record.get("yearsOfService")), record.get("country"),
                record.get("managerEmail"));
    }
}
