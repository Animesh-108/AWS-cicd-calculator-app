package com.example.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the Calculator class.
 */
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        // Create a new Calculator instance before each test
        calculator = new Calculator();
    }

    @Test
    void testAdd() {
        // Test case: 2 + 3 = 5
        assertEquals(5.0, calculator.add(2.0, 3.0), "2 + 3 should equal 5");
    }

    @Test
    void testSubtract() {
        // Test case: 10 - 4 = 6
        assertEquals(6.0, calculator.subtract(10.0, 4.0), "10 - 4 should equal 6");
    }

    @Test
    void testMultiply() {
        // Test case: 3 * 7 = 21
        assertEquals(21.0, calculator.multiply(3.0, 7.0), "3 * 7 should equal 21");
    }

    @Test
    void testDivide() {
        // Test case: 10 / 2 = 5
        assertEquals(5.0, calculator.divide(10.0, 2.0), "10 / 2 should equal 5");
    }

    @Test
    void testDivideByZero() {
        // Test case: 10 / 0 should throw an exception
        // We check that the correct exception type is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });

        // We can also check the exception message
        assertEquals("Cannot divide by zero.", exception.getMessage());
    }
}
