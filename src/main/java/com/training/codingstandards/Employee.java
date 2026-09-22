package com.training.codingstandards;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class Employee {
    public String empId;
    public String name;
    public String email;
    public String department;
    public double salary;
    public int yearsOfService;
    public String country;
    public String managerEmail;
    public Date lastProcessed;

    public Employee() {
    }

    public Employee(String empId, String name, String email, String department, double salary,
                    int yearsOfService, String country, String managerEmail) {
        this.empId = empId;
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
        this.yearsOfService = yearsOfService;
        this.country = country;
        this.managerEmail = managerEmail;
        this.lastProcessed = Date.from(Instant.now());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee other)) {
            return false;
        }
        return Objects.equals(empId, other.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId);
    }
}
