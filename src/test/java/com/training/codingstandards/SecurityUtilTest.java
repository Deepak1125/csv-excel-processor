package com.training.codingstandards;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SecurityUtilTest {
    @Test
    void hashesWithSha256AndIsDeterministic() {
        String hash = SecurityUtil.hashIdentifier("employee");
        assertEquals(64, hash.length());
        assertEquals(hash, SecurityUtil.hashIdentifier("employee"));
        assertNotEquals(hash, SecurityUtil.hashIdentifier("other"));
    }

    @Test
    void createsNonEmptyRandomTokens() {
        String first = SecurityUtil.sessionToken();
        String second = SecurityUtil.sessionToken();
        assertEquals(64, first.length());
        assertNotEquals(first, second);
    }

    @Test
    void rejectsNullHashInput() {
        assertThrows(IllegalArgumentException.class, () -> SecurityUtil.hashIdentifier(null));
    }

    @Test
    void employeeEqualityUsesIdentifierAndHashCode() {
        Employee first = new Employee("1", "A", "a@example.com", "Sales", 1, 1, "US", "m@example.com");
        Employee second = new Employee("1", "B", "b@example.com", "Finance", 2, 2, "IN", "m@example.com");
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertFalse(new HashSet<>(java.util.List.of(first)).add(second));
    }
}
