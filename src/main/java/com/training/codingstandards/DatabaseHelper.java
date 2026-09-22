package com.training.codingstandards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseHelper {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/hr";
    private static final String FIND_EMPLOYEE = "SELECT emp_id, name FROM employees WHERE emp_id = ?";
    private final String url;
    private final String user;
    private final String password;

    public DatabaseHelper() {
        this(System.getenv().getOrDefault("CSV_PROCESSOR_DB_URL", DEFAULT_URL),
                requiredSetting("CSV_PROCESSOR_DB_USER"),
                requiredSetting("CSV_PROCESSOR_DB_PASSWORD"));
    }

    DatabaseHelper(String url, String user, String password) {
        this.url = requireNonBlank(url, "Database URL");
        this.user = requireNonBlank(user, "Database user");
        this.password = requireNonBlank(password, "Database password");
    }

    public Employee findEmployee(String empId) {
        if (empId == null || empId.isBlank()) {
            throw new IllegalArgumentException("Employee ID must not be blank");
        }
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(FIND_EMPLOYEE)) {
            statement.setString(1, empId);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Employee(result.getString("emp_id"), result.getString("name"),
                            "", "", 0, 0, "", "");
                }
            }
            return null;
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to find employee", exception);
        }
    }

    public void auditExport(String userInputPath) {
        if (userInputPath == null || userInputPath.isBlank()) {
            throw new IllegalArgumentException("Audit path must not be blank");
        }
        try {
            Path path = Path.of(userInputPath).toAbsolutePath().normalize();
            if (!Files.exists(path)) {
                throw new IllegalArgumentException("Audit path does not exist: " + path);
            }
            System.out.println("Export path audited: " + path);
        } catch (java.nio.file.InvalidPathException exception) {
            throw new IllegalArgumentException("Invalid audit path", exception);
        }
    }

    private static String requiredSetting(String name) {
        return requireNonBlank(System.getenv(name), name + " environment variable");
    }

    private static String requireNonBlank(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " must be configured");
        }
        return value;
    }
}
